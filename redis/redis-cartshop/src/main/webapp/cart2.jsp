<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
<title>购物页面</title>
<style>
ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.goods li {
	display: inline-block;
	border: 1px solid #ddd;
	padding: 10px;
	margin: 10px;
}

.goods li:hover {
	background-color: #efefef;
}

.goods .price {
	color: #f00;
	font-weight: bold;
}

.goods .price::before {
	content: "￥";
}
</style>
<script>
	window.onload = function() {
		var goods = document.getElementsByClassName('goods')[0];

		// 用于保存购物车商品信息
		var carList = [];

		// 先获取当前cookie
		var cookies = document.cookie.split('; ');
		for (var i = 0; i < cookies.length; i++) {
			var arr = cookies[i].split('=');
			if (arr[0] === 'carlist') {
				carList = JSON.parse(arr[1]);
			}
		}

		// 事件委托
		goods.onclick = function(e) {
			e = e || window.event;
			var target = e.target || e.srcElement;

			// 添加到购物车
			if (target.tagName.toLowerCase() === 'button') {

				// 获取当前li
				var currentLi = target.parentElement.parentElement;
				var children = currentLi.children;
				var currentGUID = currentLi.getAttribute('data-guid');

				// 先创建一个对象保存当前商品信息
				var goodsObj = {};
				goodsObj.guid = currentGUID;
				goodsObj.qty = 1;
				goodsObj.name = children[1].innerHTML;
				goodsObj.price = children[2].innerHTML;
				goodsObj.imgUrl = children[0].src;

				// 如果cookie为空，则直接添加
				if (carList.length === 0) {
					// 添加到carList
					carList.push(goodsObj);
				} else {
					// 先判断cookie中有无相同的guid商品
					for (var i = 0; i < carList.length; i++) {
						// 如果商品已经存在cookie中，则数量+1
						if (carList[i].guid === currentGUID) {
							carList[i].qty++;
							break;
						}
					}

					// 如果原cookie中没有当前商品
					if (i === carList.length) {
						// 添加到carList
						carList.push(goodsObj);
					}

				}
				// 存入cookie
				// 把对象/数组转换诚json字符串：JSON.stringify()
				document.cookie = 'carlist=' + JSON.stringify(carList);
			}

		}
	}
</script>
</head>
<body>
	<ul class="goods">
		<li data-guid="g01"><img src="images/shirt_1.jpg">
			<p>短袖衬衣</p>
			<p class="price">98.88</p>
			<div class="add2cart">
				<button>添加到购物车</button>
			</div></li>
		<li data-guid="g02"><img src="images/shirt_2.jpg">
			<p>短袖衬衣2</p>
			<p class="price">88.88</p>
			<div class="add2cart">
				<button>添加到购物车</button>
			</div></li>
		<li data-guid="g03"><img src="images/shirt_3.jpg">
			<p>短袖衬衣3</p>
			<p class="price">9.98</p>
			<div class="add2cart">
				<button>添加到购物车</button>
			</div></li>
		<li data-guid="g04"><img src="images/shirt_4.jpg">
			<p>短袖衬衣4</p>
			<p class="price">8.88</p>
			<div class="add2cart">
				<button>添加到购物车</button>
			</div></li>
	</ul>
	<a href="04car.html">去结算</a>
</body>
</html>
