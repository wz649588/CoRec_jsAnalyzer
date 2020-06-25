
function removeKeys(obj, keys){
    var index;
    for (var prop in obj) {
        // important check that this is objects own property
        // not from prototype prop inherited
        if(obj.hasOwnProperty(prop)){
            switch(typeof(obj[prop])){
                case 'string':
                    index = keys.indexOf(prop);
                    if(index > -1){
                        delete obj[prop];
                    }
                    break;
                case 'object':
                    index = keys.indexOf(prop);
                    if(index > -1){
                        delete obj[prop];
                    }else{
                        removeKeys(obj[prop], keys);
                    }
                    break;
            }
        }
    }
    return obj;
}

var fs = require('fs');

var jsonFile = process.argv[2];
var key = process.argv[3];

var jsonCode = fs.readFileSync(jsonFile,'utf8');

var obj = JSON.parse(jsonCode);

var newObj = removeKeys(obj, key);

var newJsonCode = JSON.stringify(newObj,null,2);


fs.writeFile("/users/zijianjiang/Documents/esprima/deletedLoc.json",newJsonCode,(err)=> {
    if(err) console.log(err);
    console.log('Successfully Written to File.');
});