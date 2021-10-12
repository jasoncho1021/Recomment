document.addEventListener("DOMContentLoaded", function () {
	var url = "http://localhost:8080/api/comments";
	sendAJAX(url, dummyMain);
});

var nxt = 0;

function makeDom(val) {
	var mem = nxt;
	var treeNode = document.querySelector("#replyList").innerHTML;
	var res = "";

	if (mem > 0) {
		res = "<li>";
		if (val[mem].comment_order.length == 2) {
			res = res + treeNode.replace("<div class=\"holine\"></div>", "")
				.replace("{commentOrder}", val[mem].comment_order)
				.replace("{id}", val[mem].id)
				.replace("{content}", val[mem].text);
		} else {
			res = res + treeNode.replace("{commentOrder}", val[mem].comment_order)
				.replace("{id}", val[mem].id)
				.replace("{content}", val[mem].text);
		}
		nxt++;
		while (nxt < val.length) {
			if (val[mem].comment_order.length + 2 == val[nxt].comment_order.length) {
				res = res + '<ul>' + makeDom(val) + '</ul>';
			} else {
				break;
			}
		}
		res += "</li>";
	} else {
		nxt++;
		while (nxt < val.length) {
			if (val[mem].comment_order.length + 2 == val[nxt].comment_order.length) {
				res = res + makeDom(val);
			} else {
				break;
			}
		}
	}
	return res;
}

// not connect to server again to drawing new reply
function addReplyJs(parentli, val) {
	var template = document.querySelector('#replyList').innerHTML;
	var resHTML = "<ul><li>";
	resHTML = resHTML + template.replace('{commentOrder}', val.comment_order)
		.replace('{id}', val.id)
		.replace('{content}', val.text);
	resHTML = resHTML + "</li></ul>";
	parentli.innerHTML += resHTML;
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

function dummyMain(val) {
	val.splice(0, 0, {
		id: "-1",
		text: "root",
		comment_order: ""
	});
	var root = makeDom(val);
	var ul = document.querySelector('#outterUl li ul');
	ul.innerHTML = root;
}

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
		comment_order: cmntOrder
	}));
}

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