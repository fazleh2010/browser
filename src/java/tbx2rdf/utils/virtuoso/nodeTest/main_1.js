`use strict`;
const sparql = require('sparql');
const uri_virtuoso = "http://localhost:8890/";


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));




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