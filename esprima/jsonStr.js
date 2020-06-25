var fs = require('fs');
var fileNamejson = process.argv[2];
var output = process.argv[3];

var code = fs.readFileSync(fileNamejson,'utf8');
var data = JSON.parse(code);
var formatedData = JSON.stringify(data, null, 2);
fs.writeFile(output,formatedData,(err)=> {
    if(err) console.log(err);
    console.log('Successfully Written to File.');
});