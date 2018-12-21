/**
 * 
 */
;
//트리 변환 메서드

function getParentById(arrayList, id) {
	  return arrayList.filter(
	      function(arrayList){return arrayList.id == id;}
	  );
}

function myFunction() {
	
var treeModel = function (arrayList) {	
	var rootNodes = [];
	var traverse = function (nodes, item, index) {
		if (nodes instanceof Array) {
			return nodes.some(function (node) {
  				var len = item.id.length;
    				var parentId = item.id.substring(0,len-2);
				if (node.id == parentId) {
					node.children = node.children || [];
					return node.children.push(arrayList.splice(index, 1)[0]);
				}

				return traverse(node.children, item, index);
			});
		}
	};

	while (arrayList.length > 0) {
		arrayList.some(function (item, index) {
			if (item.id.length == 2) {
				return rootNodes.push(arrayList.splice(index, 1)[0]);
			}

			return traverse(rootNodes, item, index);
		});
	}

	return rootNodes;
};

//트리로 변환할 배열
var list =  [
	{ label : "subUser1", id : "00"},
	{ label : "subUser1-1", id : "0000"},
	{ label : "subUser1-1-1", id : "000000"},
	{ label : "subUser1-2", id : "0001"},
	{ label : "subUser2", id : "01"},
	{ label : "subUser2-1", id : "0100"},
	{ label : "subUser3", id : "02"},
	{ label : "subUser4", id : "03"}
];
document.getElementById("demo1").innerHTML = JSON.stringify(list, null, '   '); 

//트리 모델로 변환
var tree = treeModel(list, null);		

var parentId = "00";
var cnt = 2;
var searchId = parentId.substring(0,cnt);
var arr = tree; 
var sib;
while( parentId.length >= cnt ) {
    sib = getParentById(arr, searchId);
    arr = sib[0].children;
   cnt += 2;
   searchId = parentId.substring(0,cnt);   
}


//document.getElementById("demo2").innerHTML = JSON.stringify(tree, null, '   ');
document.getElementById("demo2").innerHTML = JSON.stringify(sib[0].children.length);
}

