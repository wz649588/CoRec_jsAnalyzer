var fileNamejs = process.argv[2];
var fileNamejson = process.argv[3];

var esprima = require('esprima'),
    fs = require('fs');
var code = fs.readFileSync(fileNamejs,'utf8');


function analyzeCodeForModule(code) {
    var ast = esprima.parseScript(code, {range: true, tokens : true});
    var json = JSON.stringify(ast, null, 2);
    fs.writeFile(fileNamejson,json,(err)=> {
        if(err) console.log(err);
        console.log('Successfully Written to File.');
    });
}


analyzeCodeForModule(code);