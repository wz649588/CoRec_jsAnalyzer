
var fileNamejs = process.argv[2];

var esprima = require('esprima');
    fs = require('fs');
var code = fs.readFileSync(fileNamejs,'utf8');

function analyzeCodeForModule(code) {
    var ast = esprima.parseScript(code, {range: true, tokens : true});
    var json = JSON.stringify(ast, null, 2);
    console.log(json);
}


analyzeCodeForModule(code);
// var display = function(name) {
//     print("Hello, I am a Javascript display function",name);
//     return "display function return"
//     }