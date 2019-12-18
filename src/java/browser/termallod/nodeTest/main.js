`use strict`;
const express = require('express')
const app = express()
const util = require('util');
const path = require('path'); 
const bodyParser = require('body-parser');
const multer  = require('multer')
const upload = multer({ dest: 'uploads/' /*, limits: { fileSize: 1024*1024*5000 }*/ })
const fsp = require('fs').promises;
const spawn = require('child_process').spawn;
const process = require('process');
const sparql = require('sparql');
const proxy = require('express-http-proxy'); // https://www.npmjs.com/package/express-http-proxy
const jsdom = require("jsdom"); // https://www.npmjs.com/package/jsdom
const jquery = require("jquery");

const port = 8080;
const uri_virtuoso = "http://localhost:8890/";
const uri_pubby = "http://localhost:9000/";
//const port = 8003

app.use(bodyParser.json() );                        
app.use(bodyParser.urlencoded({extended: true})); 

if (!('toJSON' in Error.prototype))
Object.defineProperty(Error.prototype, 'toJSON', {
    value: function () {
        var alt = {};

        Object.getOwnPropertyNames(this).forEach(function (key) {
            alt[key] = this[key];
        }, this);

        return alt;
    },
    configurable: true,
    writable: true
});

app.set('json spaces', 2);

app.get('/test', (req, res) => {
	res.send("pong");
})

app.get('/demo', (req, res) => {
	var demoHtml = '<form id        =  "uploadForm" ' + 
		' enctype   =  "multipart/form-data" ' +
		' action    =  "./initialize" ' + 
		' method    =  "post"> ' + 
		' <input type="file" name="upload" placeholder="tbx file" required /> ' +
		' <input type="file" name="mappings" placeholder="mappings" /> ' +
		' <input type="text" name="graphname" placeholder="graph name" /> ' +
		' <input type="submit" value="Upload file" name="submit"> ' +
		' </form>';
	console.log("/demo: serving")
	res.send(demoHtml)
})

app.get("/testsparql", async function (req, res, next) {

});



app.get("/status", async function (req, res, next) {
    res.status(200);
    res.set('Content-Type', 'text/html');

    let header = await fsp.readFile("/tmp/server/static/header.html");
    let footer = await fsp.readFile("/tmp/server/static/footer.html");

    let content = null;

    const valid_content = ['status', 'sparql', 'browser'];
    let requested = req.query.view || "status";
    if (!requested || valid_content.indexOf(requested) === -1) {
        requested = "status";
    }
    content = await fsp.readFile("/tmp/server/static/" + requested + ".html");
    
    res.write(header);
    if (content) {
        res.write(content);
    }
    res.write(footer);
    res.end();
});

app.get("/health_check", (req, res, next) => {
	res.send("OK");
});

app.get("/descriptor", (req, res, next) => {
    res.type("application/x-yaml");
    res.sendFile("/tmp/server/static/descriptor.yaml");
});

app.post("/sparql.json", async (req, res, next) => {
	try {
        let result = {query: req.body.query || null, result: null, error: null};
        if (!result.query) {
            return next(Error("no query"));
        }

        console.log("SPARQL request: " + result.query);
        var client = new sparql.Client("http://localhost:8890/sparql");
        
        client.rows(result.query, function (err, qres) {
            if (err) {
                result.error = err.toString();
            }
            if (qres && qres.length) {
                result.result = [];
                for (let i = 0; i < qres.length; i++) {
                    result.result.push(qres[i]);
                }
            }
            console.log("full result", result);
            res.json(result);
        });
	} catch (e) {
		next(e)
	}
});

app.get("/status.json", async (req, res, next) => {
	try {
		res.json(statusInformation);
	} catch (e) {
		next(e)
	}
});

const curFilepath = "/tmp/server/uploads/current.tbx";
const conversionTarget = "/tmp/server/uploads/current.ttl";
const turtleTarget = "/tmp/server/uploads/current.ttl.gz";
const MAX_LOG_LINES = 20;
var statusInformation = {"status": "container-alive", "log": []};

var setTitle = "dataset";

function log_timestamp() {
    var str = "";

    var currentTime = new Date()
    var hours = currentTime.getHours()
    var minutes = currentTime.getMinutes()
    var seconds = currentTime.getSeconds()

    if (minutes < 10) {
        minutes = "0" + minutes
    }
    if (seconds < 10) {
        seconds = "0" + seconds
    }
    str += hours + ":" + minutes + ":" + seconds + " ";
    return str;
}


function logstatus(...args) {
	if (!args || !args.length) { return; }

	let line = log_timestamp() + "\t" + args.join(" ");
	statusInformation.log.push(line);
	while (statusInformation.log.length > MAX_LOG_LINES) {
		statusInformation.log.shift();
	}
	console && console.log("status", args.join(" "));
}

logstatus("container initialized");

async function tbx2rdf(data) {
	logstatus("starting tbx2rdf", curFilepath);
	
	data['status'] = 'active';
	data['filename'] = curFilepath;

	try {
		await fsp.unlink(conversionTarget);
	} catch (ignored) {}

	/*var data = await fsp.readFile(curFilepath);
		data = data.toString();
		console.log(data); */

	const cmdExec = "java";
	const cmdArgs = ["-Xms512M", "-Xmx50G", "-jar", "/tmp/target/tbx2rdf-0.1.jar", curFilepath, "--lenient=true", "--output=" + conversionTarget];
    if ('datanamespace' in data && data['datanamespace']) {
        cmdArgs.push("--datanamespace=" + data['datanamespace']);
    }
    console.log(data);
    console.log(cmdArgs);
	const execOptions = {cwd: "/tmp"}; //, stdout: process.stderr, stderr: process.stderr};
	data['status'] = 'active';
	logstatus("starting execution of tbx2rdf");
	var result = null;

	try { 
		result = await streamExec("tbx2rdf", cmdExec, cmdArgs, execOptions);
		data.exit_code = result.code;
		if (data.exit_code != 0) {
			throw Error("exit code != 0");
		}
		logstatus("tbx2rdf exit code " + result.code);
		if (result.stdcache.stdout) { data.stdout = result.stdcache.stdout; }
		if (result.stdcache.stderr) { data.stderr = result.stdcache.stderr; }
	} catch (errconv) {
		logstatus("tbx2rdf error:", errconv.message, errconv);
		data.error = errconv;
		data.status = 'failed';
		return;
	}
	data.status = 'success';

	data.output = { "path": conversionTarget, "stat": !!(await fsp.stat(conversionTarget)) };
	data.links.push({'path': './files/current.ttl', 'title': 'RDF file', "type": "download"});

	/*var data = await fsp.readFile(conversionTarget);
		data = data.toString();
		console.log(data);*/
	return true;
}

async function streamExec(tag, cmd, args, options) {
	return new Promise(function (resolve, reject) {
		const proc = spawn(cmd, args, options);
		const stdcache = {stdout: [], stderr: []}

		proc.stdout.on('data', (data) => {
			data = data.toString().trim();
			if (data == '') return;
			data.split("\n").forEach(function(line) {
				if (line === '') return;
				logstatus(`[${tag}|stdout] ${line}`);
				stdcache.stdout.push(line);
			});
		});
		proc.stderr.on('data', (data) => {
			data = data.toString().trim();
			if (data == '') return;
			data.split("\n").forEach(function(line) {
				if (line === '') return;
				logstatus(`[${tag}|stderr] ${line}`);
				stdcache.stderr.push(line);
			});
		});
		proc.on('exit', function (code) {
			logstatus(`[${tag}|exit] code: ${code}`);
			resolve({code: code, stdcache: stdcache});
		});
		proc.on('error', function (err) {
			reject(err);
		});
	});
}

async function rdf2gzip(data) {
	data.status = 'active';
	//const cmd = "rapper -o ntriples " + conversionTarget + " | gzip >> " + turtleTarget;
	//const execOptions = {cwd: "/tmp", shell: true};
	const cmd = "gzip -f " + conversionTarget;
	const cmdArgs = []
	const execOptions = {cwd: "/tmp/server", "shell": true}; //, stdout: process.stderr, stderr: process.stderr};
	var result = null;
	logstatus("starting execution of rdf2gzip");

	try { 
		result = await streamExec("rdf2gzip", cmd, cmdArgs, execOptions);
		data.exit_code = result.code;
		if (data.exit_code != 0) {
			throw Error("exit code != 0");
		}
		logstatus("rdf2gzip exit code " + result.code);
		// data.message = "123\n4541\nimi1ko"
		// throw Error("test");
		
		if (result.stdcache.stdout) { data.stdout = result.stdcache.stdout; }
		if (result.stdcache.stderr) { data.stderr = result.stdcache.stderr; }
	} catch (errconv) {
		logstatus("rdf2gzip error:", errconv);
		data.error = errconv;
		data.status = 'failed';
		return;
	}
	data.status = 'success';
	data.output = { "path": turtleTarget, "stat": !!(await fsp.stat(turtleTarget)) };
	data.links.push({'path': './files/current.ttl.gz', 'title': 'zipped turtle file', "type": "download"});
	return true;
}

async function rdf2nt(data) {
	data.status = 'active';
	const cmdExec = "php";
	const cmdArgs = ["convert.php"];
	const execOptions = {cwd: "/tmp/server"}; //, stdout: process.stderr, stderr: process.stderr};
	//const cmd = "rapper -o ntriples " + conversionTarget + " | gzip >> " + turtleTarget;
	//const execArgs = []
	//const execOptions = {cwd: "/tmp", shell: true};
	var result = null;
	logstatus("starting execution of rdf2nt");

	try { 
		result = await streamExec("rdf2nt", cmdExec, cmdArgs, execOptions);
		data.exit_code = result.code;
		if (data.exit_code != 0) {
			throw Error("exit code != 0");
		}
		logstatus("rdf2nt exit code " + result.code);
		// data.message = "123\n4541\nimi1ko"
		// throw Error("test");
		
		if (result.stdcache.stdout) { data.stdout = result.stdcache.stdout; }
		if (result.stdcache.stderr) { data.stderr = result.stdcache.stderr; }
	} catch (errconv) {
		logstatus("rdf2nt error:", errconv);
		data.error = errconv;
		data.status = 'failed';
		return;
	}
	data.status = 'success';
	data.output = { "path": turtleTarget, "stat": !!(await fsp.stat(turtleTarget)) };
	data.links.push({'path': './files/current.nt.gz', 'title': 'n-triples file', "type": "download"});
	return true;
}

async function writeVirtuosoBackend(data) {
	data.status = 'success';
	return true;
}

async function startVirtuosoServer(data) {
	logstatus("starting data import", turtleTarget);
	
	data['status'] = 'active';
	data['filename'] = turtleTarget;
    // isql-v 1111 dba dba VERBOSE=OFF import.db
	const cmdExec = "isql-v";
	const cmdArgs = ["1111", "dba", "dba", "VERBOSE=OFF", "import.db"];
	const execOptions = {cwd: "/tmp/server"}; //, stdout: process.stderr, stderr: process.stderr};
	data['status'] = 'active';
	logstatus("starting execution of import isql");
	var result = null;

	try { 
		result = await streamExec("isql", cmdExec, cmdArgs, execOptions);
		data.exit_code = result.code;
		if (data.exit_code != 0) {
			throw Error("exit code != 0");
		}
		logstatus("isql exit code " + result.code);
		if (result.stdcache.stdout) { data.stdout = result.stdcache.stdout; }
		if (result.stdcache.stderr) { data.stderr = result.stdcache.stderr; }
	} catch (errconv) {
		logstatus("isql error:", errconv.message, errconv);
		data.error = errconv;
		data.status = 'failed';
		return;
	}
	data.status = 'success';

	data.output = { "path": turtleTarget, "stat": !!(await fsp.stat(turtleTarget)) };
	data.links.push({'path': './list', 'title': 'entity list', "type": "exthref"});
	data.links.push({'path': './sparql', 'title': 'SPARQL endpoint', "type": "exthref"});
	return true;
}


const pipeline = [
	{"name": "tbx to rdf",
	"function": tbx2rdf},
    {"name": "rdf to ttl.gz",
    "function": rdf2gzip},
	{"name": "virtuoso backend configuration",
	"function": writeVirtuosoBackend},
	{"name": "data import",
	"function": startVirtuosoServer}
];
//	{"name": "rdf to ntriples",
//	"function": rdf2nt},

function initialize_pipeline() {
	statusInformation.pipeline = {'active': false, 'current': 0, 'length': 0, 'elements': []};
	for (let processStep = 0, processSteps = pipeline.length; processStep < processSteps; processStep++) {
		var pipelineElement = pipeline[processStep];
		statusInformation.pipeline.length = processSteps;
		statusInformation.pipeline.elements.push(pipelineElement.name);
	}
}
initialize_pipeline();
logstatus("processing pipeline loaded");

async function startConversion() {
	try {
		initialize_pipeline();
		logstatus("processing pipeline started");
		statusInformation.pipeline.active = true;
		var docontinue = true;
		for (let processStep = 0, processSteps = pipeline.length; docontinue && processStep < processSteps; processStep++) {
			var pipelineElement = pipeline[processStep];
			if (!statusInformation.hasOwnProperty(pipelineElement.name)) {
				statusInformation[pipelineElement.name] = {'id': processStep, 'status': 'initializing', 'links': []};
			}
			var dataElement = statusInformation[pipelineElement.name];

            if (!dataElement) {
                dataElement = {}
            }
            if (statusInformation.state) {
                let state_keys = Object.keys(statusInformation.state);

                for (let i = 0; i < state_keys.length; i++) {
                    let key = state_keys[i];
                    dataElement[key] = statusInformation.state[key];
                }
            }

			statusInformation.pipeline.current = processStep;
			if (!pipelineElement['function']) {
				throw Error("pipeline misconfigured: no function for step " + pipelineElement.name);
			}

			dataElement.time_start = +(new Date());
			try {
				logstatus("starting pipeline step", pipelineElement.name);
				docontinue = await pipelineElement['function'](dataElement);
				logstatus("pipeline step", pipelineElement.name, "done");
			} catch (plErr) {
				docontinue = false;
				logstatus("pipeline step " + processStep + " failed", plErr);
				dataElement.status = 'failed';
			}

			dataElement.time_end = +(new Date());
			dataElement.duration = (dataElement.time_end - dataElement.time_start) / 1000.0;

			if (dataElement && dataElement.status === 'active') {
				dataElement.status = 'done';
			}
		}
/*		var docontinue = false;
		docontinue = await tbx2rdf();
		if (docontinue) {
			docontinue = await rdf2nt();
		}
		if (docontinue) {
			docontinue = await writeVirtuosoBackend();
		}
*/
		statusInformation['status'] = 'pipeline-complete';
		statusInformation.pipeline.active = false;
	} catch (err) {
		statusInformation['status'] = 'error';
		statusInformation['error'] = err;
		statusInformation.pipeline.active = false;
		logstatus("error", err);
	}
}

function getFile(req, fieldname) {
    if (!req || !req.files || !req.files.length) {
        return null;
    }
    for (let idx = 0; idx < req.files.length; idx++) {
        let f = req.files[idx];
        if (f && f.fieldname && f.fieldname === fieldname) {
            return f;
        }
    }
    return null;
}

app.get("/initialize", async (req, res, next) => {
    res.status(401).send('invalid method, endpoint requires a POST request');
});

app.post('/initialize', upload.any(), async (req, res, next) => {
	// req.files hold all uploaded files (upload, optionally mapping)
	// req.body will hold the text fields, if there were any
	try {
		statusInformation['status'] = 'initializing';
        statusInformation['state'] = {};

        let upload = getFile(req, "upload");
        if (!upload) {
            throw Exception("no upload data in call");
        }
		statusInformation['tmp_file'] = upload.path.toString();
		console.log("/initialize processing", upload.path);
		var moveResult = await fsp.rename(upload.path, curFilepath);

        let mapping = getFile(req, "mapping");
        if (mapping) {
            statusInformation['tmp_map_file'] = mapping.path.toString();
            console.log("got mapping information", mapping.path, curFilepath + ".mapping");
            var moveResult = await fsp.rename(mapping.path, curFilepath + ".mapping");
        } else {
            statusInformation['tmp_map_file'] = null;
        }

		if (req.body && req.body.title) {
			setTitle = req.body.title.substring(0, 100);
		}
        if (req.body && req.body.datanamespace) {
            statusInformation.state.datanamespace = req.body.datanamespace;
        }

		res.send("/initialize converting now. check /status");
		statusInformation['status'] = 'conversion-init';
		setTimeout(startConversion, 0);
	} catch (e) {
		next(e);
	}
})

/*app.get('/', (req, res) => {
	res.send('OpenAPI provider 1')
})*/
app.use('/files', express.static('/tmp/server/uploads/'));

app.use('/static', express.static(path.join(__dirname, 'static'), {
	index: false
}));

function needs_rewrite(uri) {
    if (!uri) { return false; }
    uri = uri.toLowerCase().trim();
    if (uri.startsWith("http://localhost") || uri.startsWith("https://localhost")) { return true; }
    if (uri.startsWith("http:") || uri.startsWith("https:")) { return false; }
    return true;
}

function rewrite_uri(base_uri, prefix, uri) {
    if (!uri) { return uri; }
    
    if (uri.startsWith("http://localhost") || uri.startsWith("https://localhost")) {
        uri = uri.replace("http://localhost", "").replace("https://localhost", "");
        if (uri.indexOf("/") > -1) {
            uri = uri.substring(uri.indexOf("/"));
        }
    }
    if (base_uri && !uri.startsWith("/")) {
        return uri;
    }

    if (!uri.startsWith("/")) {
        return uri;
    }
    if (prefix.endsWith("/")) {
        prefix = prefix.substring(0, prefix.length - 1);
    }

    return prefix + uri;
}

function rewriteVirtuoso (proxyRes, proxyResData, userReq, userRes) {

    let rewritePrefix = null;
    if (userReq && userReq.headers && userReq.headers['x-context']) {
        rewritePrefix = userReq.headers['x-context'];
    }
    if (!rewritePrefix) {
        return proxyResData;
    }

    if (proxyRes && proxyRes.headers && proxyRes.headers['content-type'] && proxyRes.headers['content-type'].indexOf("text/html") > -1) {
        let data = proxyResData.toString('utf8');

        const dom = new jsdom.JSDOM(data);
        const $ = jquery(dom.window);

        let base_uri = null;
        $("base").each(function(idx, elem) {
            if (!elem) return;
            let $elem = $(elem);
            let href = $elem.attr("href");
            if (!href) { return; }
            base_uri = href;
        });

        if (base_uri) {
            // make protocol relative
            if (base_uri.startsWith("http://")) {
                base_uri = "//" + base_uri.substring("http://".length);
            }
            if (base_uri.startsWith("https://")) {
                base_uri = "//" + base_uri.substring("https://".length);
            }
            // add in rewritePrefix
            if (rewritePrefix) {
                let bu_delim = base_uri.indexOf("/", 3);
                if (bu_delim > -1) {
                    let base_rewrite = rewritePrefix;
                    if (base_rewrite.endsWith("/")) {
                        base_rewrite = base_rewrite.substring(0, base_rewrite.length - 1);
                    }
                    base_uri = base_uri.substring(0, bu_delim) + base_rewrite + base_uri.substring(bu_delim);
                }
            }

            $("base").each(function(idx, elem) {
                let $elem = $(elem);
                $elem.attr('href', base_uri);
            });
        }

        function rewrite_href(idx, elem){
            let $elem = $(elem);
            if ($elem.attr("href")) {
                let href = $elem.attr("href");
                
                if (elem.tagName === "BASE") {
                    if (href.startsWith("http:")) {
                        href = href.substring("http:".length);
                    }
                    if (href.startsWith("https:")) {
                        href = href.substring("https:".length);
                    }
                    $elem.attr("href", href);
                    return;
                }

                if (!needs_rewrite(href)) { return; }
                
                href = rewrite_uri(base_uri, rewritePrefix, href);
                $elem.attr("href", href);
            }
        }
        
        function rewrite_src(idx, elem){
            let $elem = $(elem);
            if ($elem.attr("src")) {
                let imgsrc = $elem.attr("src");
                if (!needs_rewrite(imgsrc)) { return; }

                imgsrc = rewrite_uri(base_uri, rewritePrefix, imgsrc);
                $elem.attr("src", imgsrc);
            }
        }
        
        function rewrite_action(idx, elem){
            let $elem = $(elem);
            if ($elem.attr("action")) {
                let actionuri = $elem.attr("action");
                if (!needs_rewrite(actionuri)) { return; }

                actionuri = rewrite_uri(base_uri, rewritePrefix, actionuri);
                $elem.attr("action", actionuri);
            }
        }

        $("a").each(rewrite_href);
        $("link").each(rewrite_href);
        $("img").each(rewrite_src);
        $("script").each(rewrite_src);
        $("form").each(rewrite_action);
        
        let result_text = dom.window.document.documentElement.outerHTML;

        var urlRegex =/(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
        var scriptHrefRegex = /href=[\'\"]?.*[\']/ig;
        result_text = result_text.replace(urlRegex, function(uri) {
            if (uri.indexOf(rewritePrefix) > -1) {
                return uri;
            }

            uri = rewrite_uri(base_uri, rewritePrefix, uri);
            return uri;
        });
        result_text = result_text.replace(scriptHrefRegex, function(txt) {
            if (!txt) return txt;
            if (txt.indexOf(rewritePrefix) > -1) return txt;
            let uri = txt.substring("href='".length, txt.length - 1).trim();
            uri = rewrite_uri(base_uri, rewritePrefix, uri);
            txt = "href='" + uri + "'"
            return txt;
        });

        return result_text;

    }
    return proxyResData;
}

// proxy unknown request paths through the proxy middleware for handling in Virtuoso
// if this fails (Virtuoso not started, Virtuoso does not handle that URI, ...) fallback to a 404
var proxyVirtuoso = proxy(uri_virtuoso, {
    proxyReqPathResolver: function (req) {
        var reqpath = req.url;
        console.log("virtuoso request: ", reqpath);
        return reqpath;
    },
    userResDecorator: rewriteVirtuoso
});
var proxyPubby = proxy(uri_pubby, {
    proxyReqPathResolver: function (req) {
        var parts = req.url.split('?');
        var queryString = parts[1];
        var updatedPath = parts[0].replace("/pubby/", "/");
        let respath = updatedPath + (queryString ? '?' + queryString : '');
        return respath;
    }
});
app.use("/", function (req, res, next) {
    if (req.originalUrl && req.originalUrl.startsWith("/pubby/")) {
        console.log(`dispatching ${req.originalUrl} to pubby proxy`);
        return proxyPubby(req, res, function proxyNext(proxyError) {
            if (proxyError && proxyError instanceof Error) {
                console.log(`proxy::error ${proxyError.message} (${proxyError.syscall} for ${proxyError.address}:${proxyError.port})`)
                return next();
            }
        });
    }

	console.log(`dispatching ${req.originalUrl} to virtuoso proxy`);
	return proxyVirtuoso(req, res, function proxyNext(proxyError) {
		if (proxyError && proxyError instanceof Error) {
			console.log(`proxy::error ${proxyError.message} (${proxyError.syscall} for ${proxyError.address}:${proxyError.port})`)
			return next();
		}
	});
});
app.use(function (req, res, next) {
	var fullUrl = req.protocol + '://' + req.get('host') + req.originalUrl;
	console.log("404", fullUrl);
	console.log(req.url);
	console.log(req.originalUrl);
	console.log(req.path);
	res.status(404).send("404 - Page not found")
})

app.listen(port, '0.0.0.0', () => console.log(`Instance listening on port ${port}!`))

