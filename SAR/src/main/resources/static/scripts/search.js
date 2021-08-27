let canv, brush;
let possX = [];
let possY = [];
let centX;
let centY;

const speed = 3;
const bubbles = 30;
window.onload = () => {
    canv = document.querySelector("canvas");
    centX = (canv.width = canv.clientWidth) / 2;
    centY = (canv.height = canv.clientHeight) / 2;

    brush = canv.getContext("2d");

    for (let i = 0; i < bubbles; i++) {
	let rad = 100 + (Math.random() * 50);
	let ang = Math.random() * Math.PI;
	possX[i] = centX + rad * Math.cos(ang);
	possY[i] = centY + rad * Math.sin(ang);
    }

    window.requestAnimationFrame(animate);
};

function animate() {
    brush.fillStyle = "hsl(0, 0%, 8%)";
    brush.fillRect(0, 0, canv.width, canv.height);
    brush.fillStyle = "hsla(0, 0%, 8%, 0%)";
    brush.fillRect(0, 0, canv.width, canv.height);

    brush.strokeStyle = "hsl(0, 0%, 64%)";
    for (let i = 0; i < possX.length; i++) {
    	let dx = possX[i] - centX;
	let dy = possY[i] - centY;
	let dm = magnitude(dx, dy);
	possX[i] += speed * dx / dm;
	possY[i] += speed * dy / dm;

	let dist = magnitude(dx ,dy);
	brush.beginPath();
	brush.arc(possX[i], possY[i], Math.sqrt(dist), 0, 2 * Math.PI);
	brush.stroke();
	if (2 * Math.abs(dx) > canv.width || 2 * Math.abs(dy) > canv.height) {
	    let rad = 100 + (Math.random() * 50);
	    let ang = Math.random() * Math.PI;
	    possX[i] = centX + rad * Math.cos(ang);
	    possY[i] = centY + rad * Math.sin(ang);
	}
    }
    window.requestAnimationFrame(animate);
}

function magnitude(dx ,dy) {
    let retrVal = Math.sqrt(dx * dx + dy * dy);
    if (retrVal <= 0) {
	retrVal = 1;
    }
    return retrVal;
}

window.onresize = () => {
    centX = (canv.width = canv.clientWidth) / 2;
    centY = (canv.height = canv.clientHeight) / 2;
};