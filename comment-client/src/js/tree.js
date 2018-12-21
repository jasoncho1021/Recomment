/**
 * 
 */
function Node(data, id) {
	this.data = data;
	this.id = id;
	//this.parent = null;
	this.children = [];
}

/*
function Tree(node) {
	this._root = node;
}
*/

var list = [
	{ label : "subUser1", id : "00"},
	{ label : "subUser1-1", id : "0000"},
	{ label : "subUser1-1-1", id : "000000"},
	{ label : "subUser1-1-2", id : "000001"},
	{ label : "subUser1-2", id : "0001"},
	{ label : "subUser2", id : "01"},
	{ label : "subUser2-1", id : "0100"},
	{ label : "subUser3", id : "02"},
	{ label : "subUser4", id : "03"}
];

var next = 0;
function makeTree() {
	var mem = next;
	var treeNode = new Node(list[mem].label, list[mem].id);
	next++;
	while ( next < list.length ) {
		if( list[mem].id.length+2 == list[next].id.length ) {
			treeNode.children.push(makeTree());
		} else {
			break;
		}
	}
	return treeNode;
}

function traversal(node) {
	var txt = JSON.stringify(node.data);
	var newDiv = document.createElement("div");
	newDiv.id = 'test';
	newDiv.innerHTML = "<p>"+txt+"</p>";
	document.getElementById('demo').appendChild(newDiv);
	for(var i =0; i < node.children.length; i++)
		traversal(node.children[i]);
}

function dummymain() {
	list.splice(0,0,{label:"root",id:""});
	/*
	var root = new Tree(makeTree());
	console.log(root._root);
	traversal(root._root);
	*/
	var root = makeTree();
	traversal(root);
	
}