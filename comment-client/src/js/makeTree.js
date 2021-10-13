document.addEventListener("DOMContentLoaded", function () {
	var url = "http://localhost:8080/api/comments";
	sendAJAX(url, dummyMain);
});

function sendAJAX(url, callback) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState == 4 && this.status == 200) {
			callback(JSON.parse(this.response));
		} else {
			console.log("ajax failure:" + this.readyState, this.status);
		}
	};
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send();
}

//var idx;
function dummyMain(val) {

	/*
	val.splice(0, 0, {
		id: "-1",
		text: "root",
		commentOrder: ""
	});

	idx = -1;
	*/

	console.log(val);

	var leaves = "";
	for (var node in val) {
		console.log(val[node]);
		leaves += makeTree(val[node]);
	}
	console.log(leaves);

	var tree = document.createElement("ul");
	tree.innerHTML = leaves;

	var root = document.querySelector('#outterUl li');
	root.append(tree);

	console.log("end");
}


function makeTree(val) {
	var treeNode = document.querySelector("#replyList").innerHTML;
	var subTree = "";

	subTree = "<li>";

	treeNode = treeNode
		.replace("{commentOrder}", val.comment.commentOrder)
		.replace("{id}", val.comment.id)
		.replace("{content}", val.comment.text);

	subTree += treeNode;

	var arr = val.recomments;
	if (arr.length > 0) {
		subTree += '<ul>';
		for (var node in arr) {
			console.log("re", arr[node]);
			subTree += makeTree(arr[node]);
		}
		subTree += '</ul>';
	}

	subTree += "</li>";

	return subTree;
}

// 00 문자열 고정 포맷, 서버에서 nested JSON 으로 안 주는 경우.해당 함수로 대댓글 구조 생성
function makeDom(val) {
	var treeNode = document.querySelector("#replyList").innerHTML;
	var subTree = "";
	var len = val[idx].commentOrder.length;
	var temp = idx;
	var tagEnd = false;

	if (idx > 0) {
		subTree = "<li>";

		treeNode = treeNode
			.replace("{commentOrder}", val[idx].commentOrder)
			.replace("{id}", val[idx].id)
			.replace("{content}", val[idx].text);

		subTree = subTree + treeNode;

		idx++;
		while (idx < val.length) {
			if (len + 2 == val[idx].commentOrder.length) { // 직계 자식 대댓글 
				console.log("meet child:", val[temp].commentOrder, val[idx].commentOrder);

				subTree = subTree + '<ul>' + makeDom(val) + '</ul>';
			} else if (len == val[idx].commentOrder.length) { // 형제들
				console.log("meet sibling:", val[temp].commentOrder, val[idx].commentOrder);

				subTree += "</li>";
				tagEnd = true;
				subTree = subTree + makeDom(val);
			} else { // 나보다 조상(부모 이상)
				console.log("meet acester:", val[temp].commentOrder, val[idx].commentOrder);

				if (!tagEnd) {
					subTree += "</li>";
					tagEnd = true;
				}
				break;
			}
		}

		if (idx < val.length) {
			console.log("end loop:", val[temp].commentOrder, val[idx].commentOrder);
		}
		if (!tagEnd) {
			console.log("meet last:", val[temp].commentOrder, idx);
			subTree += "</li>";
		}

	} else { // 자기 자신 li 추가 안 한다.
		idx++;
		while (idx < val.length) {
			if (len + 2 == val[idx].commentOrder.length) {
				subTree = subTree + makeDom(val); // <li></li>..<li></li>
			}
		}
	}

	return subTree;
}

// button, input event bubbling listener
var outter = document.querySelector('#outterUl');
outter.addEventListener('click', function (evt) {
	var clickedTarget = evt.target;
	console.log(clickedTarget);
	if (clickedTarget.id == 'addReply') { // open input,, div addReply
		var el = clickedTarget.querySelector('#inputReply');
		el.style.visibility = "visible";
	} else if (clickedTarget.tagName == 'BUTTON') { // send ,, button
		ajax(clickedTarget); // button
	}
});

function ajax(bttn) { //button	
	var element = bttn; //evt.currentTarget;
	var box = element.closest(".cmnt-box");
	var cmntOrder = box.getAttribute('cmntOrder');
	var url = "http://localhost:8080/api/comments";
	var textInput = box.querySelector('input').value;

	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState == 4 && this.status == 201) {
			var parentli = element.closest('li');
			addReplyJs(parentli, JSON.parse(this.response));
			console.log("success:" + JSON.parse(this.response));
		} else {
			console.log("post ajax failure:" + this.readyState, this.status);
		}
	};
	xhttp.open('POST', url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(JSON.stringify({
		text: textInput,
		commentOrder: cmntOrder
	}));
}

// not connect to server again to drawing new reply
function addReplyJs(parentli, val) {
	var template = document.querySelector('#replyList').innerHTML;
	var resHTML = "<ul><li>";
	resHTML = resHTML + template.replace('{commentOrder}', val.commentOrder)
		.replace('{id}', val.id)
		.replace('{content}', val.text);
	resHTML = resHTML + "</li></ul>";
	parentli.innerHTML += resHTML;
}