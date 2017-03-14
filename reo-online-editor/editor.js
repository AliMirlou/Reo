(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  var line, isDown, origX, origY;
  var mode = 'select';
  var id = 'a';
  
  document.getElementById("select").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'select';
    this.style.border = '2px solid black';
  };
  
  document.getElementById("component").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'component';
    this.style.border = '2px solid black';
  };
  
  document.getElementById("sync").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'sync';
    this.style.border = '2px solid black';
  };
  
  document.getElementById("downloadsvg").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.svg";
    a.href = 'data:image/svg+xml;base64,' + window.btoa(canvas.toSVG());
    a.click();
  };
  
  document.getElementById("downloadpng").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.png";
    a.href = canvas.toDataURL('image/png');
    a.click();
  };

  function makeCircle(left, top) {
    var c = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: 5,
      radius: 12,
      fill: '#fff',
      stroke: '#000',
      hasBorders: false,
      hasControls: false,
      class: 'node'
    });
    
    // give the node an identifier and increment it for the next node
    c.set({'id': id});
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');

    // these are the channels that are connected to this node
    c.linesIn = [];
    c.linesOut = [];

    return c;
  } //makeCircle

  function drawLine(x1, y1, x2, y2) {
    // create a line...
    line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'line'
    });
    
    // ...an arrowhead...
    var a = new fabric.Triangle({
      left: x2,
      top: y2,
      width: 20,
      height: 20,
      angle: calcArrowAngle(x1,y1,x2,y2),
      fill: '#000',
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default'
    });
    
    // ...and two circles
    var c1 = makeCircle(x1,y1);
    var c2 = makeCircle(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    c1.linesOut.push(line);
    c2.linesIn.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateLine(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,c1,c2);
    canvas.renderAll();
  } //drawLine
  
  function updateNode(node) {
    if (node.linesIn.length) {
      if (node.linesOut.length)
        node.set({'nodetype':'mixed','fill':'#ff5'});
      else
        node.set({'nodetype':'drain','fill':'#f55'});
    }
    else
      node.set({'nodetype':'source','fill':'#55f'});
  }
  
  function updateLine(line, end) {
    // we first have to reset the end coordinates
    var x1 = line.circle1.get('left');
    var y1 = line.circle1.get('top');
    var x2 = line.circle2.get('left');
    var y2 = line.circle2.get('top');
    line.set({'x1':x1, 'y1':y1, 'x2':x2, 'y2':y2});
    
    if (x1 == x2 && y1 == y2) {
      line.arrow.set({'left': line.get('x2'), 'top': line.get('y2')});
    }
    else {
      // calculate the position of the arrow
      var length = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
      length = length - 22;//circle2.get('radius') - circle2.get('stroke');
      var x = Math.atan(Math.abs(y1-y2)/Math.abs(x1-x2));
      if (end) {
        if (x2 > x1) {
          line.set({'x2': x1 + length * Math.cos(x)});
        } else {
          line.set({'x2': x1 - length * Math.cos(x)});
        }
        if (y2 > y1) {
          line.set({'y2': y1 + length * Math.sin(x)});
        } else {
          line.set({'y2': y1 - length * Math.sin(x)});
        }
      }
      line.arrow.set({'left': line.get('x2'), 'top': line.get('y2')});
      var angle = calcArrowAngle(line.get('x1'), line.get('y1'), line.get('x2'), line.get('y2'));
      line.arrow.set({'angle': angle});
    }
    
    canvas.renderAll();
  } //updateLine
  
  // calculate the correct angle for the arrowhead
  function calcArrowAngle(x1, y1, x2, y2) {
    var angle = 0, x, y;
    x = (x2 - x1);
    y = (y2 - y1);

    if (x === 0) {
      angle = (y === 0) ? 0 : (y > 0) ? Math.PI / 2 : Math.PI * 3 / 2;
    } else if (y === 0) {
      angle = (x > 0) ? 0 : Math.PI;
    } else {
      angle = (x < 0) ? Math.atan(y / x) + Math.PI : (y < 0) ? Math.atan(y / x) + (2 * Math.PI) : Math.atan(y / x);
    }

    return ((angle * 180 / Math.PI) + 90) % 360;
  } //calcArrowAngle
  
  function enumerate() {
    document.getElementById('text').innerHTML = "";
    canvas.forEachObject(function(obj) {
      if (obj.class == 'node')
        document.getElementById('text').innerHTML += obj.id + " ";
    });
  }
  
  function updateText() {
    var s = '';
    // TODO: traverse id's
    canvas.forEachObject(function(obj) {
      if (obj.type == "line")
        s += 'sync(' + obj.circle1.id + ',' + obj.circle2.id + ') ';
    });
    document.getElementById('text').innerHTML = s;
  }
  
  function snapToComponent(node,comp) {
    var diffWidth = comp.width / 2;
    var diffHeight = comp.height / 2;
    if (node.left - comp.left > diffWidth) // right side
      node.set({'left': comp.left + diffWidth});
    if (comp.left - node.left > diffWidth) // left side
      node.set({'left': comp.left - diffWidth});
    if (node.top - comp.top > diffHeight) // bottom side
      node.set({'top': comp.top + diffHeight});
    if (comp.top - node.top > diffHeight) // top side
      node.set({'top': comp.top - diffHeight});
    node.setCoords();
  }

  canvas.on('object:moving', function(e) {
    var p = e.target;
    p.setCoords();
  }); //object:moving
  
  canvas.on('object:added', function(e) {
    updateText();
  }); //object:added
  
  canvas.on('object:removed', function(e) {
    updateText();
  }); //object:added
  
  canvas.on('mouse:down', function(e) {
    isDown = true;
    if (canvas.getActiveObject())
      return;
    var pointer = canvas.getPointer(e.e);
    if (mode == 'sync') {
      drawLine(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateLine(line,2);
    }
    if (mode == 'component') {
      origX = pointer.x;
      origY = pointer.y;
      var comp = drawComponent(pointer.x,pointer.y,pointer.x,pointer.y);
      canvas.setActiveObject(comp);
    }
  }); //mouse:down
  
  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var p = canvas.getActiveObject();
    if (!p)
      return;
    var pointer = canvas.getPointer(e.e);
    if (p.class == 'component') {
      if (origX > pointer.x)
        p.set({left:pointer.x});
      if (origY > pointer.y)
        p.set({top:pointer.y});
      p.set({width:Math.abs(origX - pointer.x)});
      p.set({height:Math.abs(origY - pointer.y)});
      p.setCoords();
      p.label.set({left: p.left + (p.width/2), top: p.top - 20});
      
      
    }
    if (p.class == 'node') {
      p.set({'left': pointer.x, 'top': pointer.y});
      p.setCoords();
      canvas.forEachObject(function(obj) {
        if (obj !== p && obj.get('type') === "circle" && p.intersectsWithObject(obj)) {
          if(Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
            p.set({'left': obj.getLeft(), 'top': obj.getTop()});
            p.setCoords();
          }
        }
      });
      for (i = 0; i < p.linesIn.length; i++)
        updateLine(p.linesIn[i], 2);
      for (i = 0; i < p.linesOut.length; i++)
        updateLine(p.linesOut[i], 1);
    }
    canvas.renderAll();
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p && p.class == 'node') {
      p.setCoords();
      canvas.forEachObject(function(obj) {
        if (!obj || obj.get('id') == p.get('id') || obj.get('type') !== "circle")
          return;
        if (p.intersectsWithObject(obj)) {
          if(Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
            for (i = 0; i < p.linesIn.length; i++) {
              p.linesIn[i].circle2 = obj;
              obj.linesIn.push(p.linesIn[i]);
            }
            for (i = 0; i < p.linesOut.length; i++) {
              p.linesOut[i].circle1 = obj;
              obj.linesOut.push(p.linesOut[i]);
            }
            canvas.remove(p);
            updateNode(obj);
            obj.bringToFront();
            canvas.renderAll();
          }
        }
      });
      snapToComponent(p,main);
      for (i = 0; i < p.linesIn.length; i++)
        updateLine(p.linesIn[i], 2);
      for (i = 0; i < p.linesOut.length; i++)
        updateLine(p.linesOut[i], 1);
      canvas.deactivateAll();
      canvas.calcOffset();
    }
  });
  
  // Double-click event handler
  var doubleClick = function (obj, handler) {
    return function () {
      if (obj.clicked) handler(obj);
      else {
        obj.clicked = true;
        setTimeout(function () {
          obj.clicked = false;
        }, 500);
      }
    };
  };
  
  function drawComponent(x1,y1,x2,y2) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;
  
    var label = new fabric.IText('name', {
      left: left + (width / 2),
      top: top - 20
    });  
  
    var rect = new fabric.Rect({
      left: left,
      top: top,
      width: width,
      height, height,
      fill: 'transparent',
      stroke: '#000',
      strokeWidth: 1,
      hoverCursor: 'default',
      originX: 'left',
      originY: 'top',
      label: label,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      class: 'component'
    });
  
    label.on('mousedown', doubleClick(label, function (obj) {
      label.enterEditing();
      label.selectAll();
    }));  
  
    canvas.add(rect,label);
    rect.setCoords();
    canvas.renderAll();
    return rect;
  }
  
  var main = drawComponent(50,50,750,550);
  main.set({hasBorders:false,hasControls:false,selectable:false});
  drawLine(100,100,200,100);
  drawLine(300,100,400,100);

})();




