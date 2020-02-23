
<?php
$to_execute="java -jar /Users/elahi/NetBeansProjects/AHalloWorld/dist/AHalloWorld.jar";
if(exec($to_execute, $output))
   print_r($output);
else echo "failed";
?>