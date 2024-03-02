function el(id){
	//== сокращение
	return document.getElementById( id );
}

function clear_canv(){
	//== стереть всю канву
	ctx.clearRect(0, 0, canv.width, canv.height);	
}
//== группа математических функций 
function abs(x){
	return Math.abs(x);
}

function acos(x){
	return Math.acos(x);
}

function acosh(x){
	return Math.acosh(x);
}

function asin(x){
	return Math.asin(x);
}

function asinh(x){
	return Math.asinh(x);
}

function atan(x){
	return Math.atan(x);
}

function cos(x){
	return Math.cos(x);
}

function exp(x){
	return Math.exp(x);
}

function log(x){
	return Math.log(x);
}

function sign(x){
	return Math.sign(x);
}

function sin(x){
	return Math.sin(x);
}

function sqrt(x){
	return Math.sqrt(x);
}

function tan(x){
	return Math.tan(x);
}

//==расчет минимального и максимального значений функции
function calc_minmax(){
	//-- считываем границы по х и выражение для функции 
	x_left = el('x_left').value;
	x_right = el('x_right').value;
	var st = el('func').value;

	//-- готовим начальные значения переменных
	var x = x_left;
	var dx = (x_right - x_left)/200;
	var y = eval(st);
	var y_min = y;
	var y_max = y;
	
	//-- формируем минимальное и максимальное значения выражения
	for(num = 1; num <200; num++){
		x = Number(x) + dx;
		y = eval(st);
		if (y> y_max)  y_max = y;
		if (y< y_min)  y_min = y;
	}
	
	//-- выводим полученные значения
	el('func_min').value = y_min;
	el('func_max').value = y_max;
	
}

function get_params(){
	//-- считываем границы по х и y
	x_left = el('x_left').value;
	x_right = el('x_right').value;
	y_down = el('y_down').value;
	y_up = el('y_up').value;	
}

function x2canv(x){
	//-- сформировать горизонтальную координату канвы 
	return 20+(x-x_left)*540/(x_right - x_left);
}

function y2canv(y){
	//-- сформировать вертикальную координату канвы 
	return 380 - (y-y_down)*340/(y_up - y_down);	
}

function draw_graph(){
	//-- считываем границы по х и выражение для функции 
	x_left = el('x_left').value;
	x_right = el('x_right').value;
	y_down = el('y_down').value;
	y_up = el('y_up').value;
	st = el('func').value;
	pen_color = el("pencolor").value;
	var width = 2;
	var dx = (x_right - x_left)/200;
	
	//-- устанавливаем перо на начальную точку
	var x = x_left;
	var y = eval(st);
	var x_canv = x2canv(x);
	var y_canv = y2canv(y);
	
	ctx.beginPath();
	ctx.moveTo(x_canv, y_canv);
	ctx.lineWidth = width;
	ctx.strokeStyle = pen_color;
	
	//-- рисуем график
	for(num = 1; num <200; num++){
		x = Number(x) + dx;
		y = eval(st);
		
		x_canv = x2canv(x);
		y_canv = y2canv(y);
		ctx.lineTo(x_canv, y_canv);
	}
	ctx.stroke();	

	//-- рисуем ось Х
	y0_canv = 200;
	if ((y_up >= 0)&&(y_down<=0)){
		y0_canv = y2canv(0);
	} else if(y_up < 0){
		y0_canv = 10;
	} else if(y_down >0){
		y0_canv = 390;
	}
	ctx.beginPath();
	ctx.moveTo(20, y0_canv);
	ctx.lineTo(580, y0_canv);
	ctx.lineWidth = width;
	ctx.strokeStyle = 'black';
	ctx.stroke();	

	//-- рисуем ось Y
	x0_canv = 300;
	if ((x_right >= 0)&&(x_left<=0)){
		x0_canv = x2canv(0);
	} else if(x_right < 0){
		x0_canv = 590;
	} else if(x_left >0){
		x0_canv = 10;
	}
	ctx.beginPath();
	ctx.moveTo( x0_canv, 10);
	ctx.lineTo( x0_canv, 390);
	ctx.lineWidth = width;
	ctx.strokeStyle = 'black';
	ctx.stroke();	
}

function canv2x(x_canv){
	//-- определить Х для точки на канве
	//return 20+(x-x_left)*540/(x_right - x_left);
	x = Number(x_left) + (x_canv-20)*(x_right - x_left)/540;
	return x.toString().substr(0,5);
}

function canv2y(y_canv){
	//-- определить Y для точки на канве
	//return 380 - (y-y_down)*340/(y_up - y_down);
	y = Number(y_down) + (380 - y_canv)*(y_up - y_down)/340;
	return y.toString().substr(0,5);
}

function hndl_move(ev){
	var cRect = canv.getBoundingClientRect();          // прямоугольник канвы
	var x_canv = Math.round(ev.clientX - cRect.left);  // из абсолютных координат события
	var y_canv = Math.round(ev.clientY - cRect.top);   // получаем x,y относительно канвы
	var x = canv2x(x_canv);
	var y = eval(st)
	var Y = y.toString().substr(0,5);
	//var Y = canv2y(y_canv);	
	
	ctx.clearRect(520,10, 70, 70);
	ctx.fillText("X: "+x, 520, 30);
	ctx.fillText("F: "+Y, 520, 50);
	//ctx.fillText("X: "+x_canv, 520, 80);
	//ctx.fillText("Y: "+y_canv, 520, 100);
	
}

function onLoadHandler() {
	//== базовые объекты создаем при загрузке страницы
	canv  = el('canv');
	canv.addEventListener("mousemove", hndl_move); 
	ctx = canv.getContext('2d');
	ctx.font = "16px Arial";
	func_color = 'blue';
	get_params();
	
	/*
	canv.addEventListener("mousemove", function(ev) { 
		var cRect = canv.getBoundingClientRect();        // Gets CSS pos, and width/height
		var x_canv = Math.round(ev.clientX - cRect.left);  // Subtract the 'left' of the canvas 
		var y_canv = Math.round(ev.clientY - cRect.top);   // from the X/Y positions to make  
		var X = canv2x(x_canv);
		var Y = canv2y(y_canv);	

		ctx.clearRect(540,0, 580,50);
		ctx.fillText("X: "+x_canv, 540, 20);
		ctx.fillText("Y: "+y_canv, 540, 40);
		ctx.fillText("X: "+X, 540, 20);
		ctx.fillText("Y: "+Y, 540, 40);
	});
	*/

}
window.onload = onLoadHandler;

