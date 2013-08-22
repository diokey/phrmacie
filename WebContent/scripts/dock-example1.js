$(function () { 
	// Dock initialize
	$('#dock').Fisheye(
		{
			maxWidth: 20,
			items: 'a',
			itemsText: 'span',
			container: '.dock-container',
			itemWidth: 50,
			proximity: 60,
			alignment : 'left',
			valign: 'bottom',
			halign : 'center'
		}
	);
});