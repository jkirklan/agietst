/*
 * canvasapp.js
 */

function PlaneTracker(document, baseImage, canvasId) {
  this.baseImage = baseImage;
  this.canvas = document.getElementById(canvasId);
  this.width = this.canvas.width;
  this.height = this.canvas.height;
  this.context = this.canvas.getContext("2d");
  this.should_run = true;
  this.initializeContext(this.context);
}
PlaneTracker.prototype = {
  initializeContext: function(ctx) {
    ctx.fillStyle = '#ffffff';
    ctx.strokeStyle = '#ff00ff';
    ctx.lineWidth = 2;
  },
  drawRect : function(x, y, width, height) {
    this.context.strokeRect(x - (width / 2), y, width, height);
  },
  register: function() {
    var planeTracker = this;
    $.ajax("/agie-services/rest/agie-test-poller/register", {type: "GET"})
      .done(function(data){
        planeTracker.regId = data.message;
        planeTracker.poll_for_updates();
      });
  },
  toggle: function() {
    this.should_run = !this.should_run;
    if(this.should_run) {
      this.register();
    }
  },
  
  get_last_location : function() {
    var planeTracker = this;
    $.ajax("/agie-services/rest/agie-test-poller/" + planeTracker.regId, {
      type : "GET"
    }).done(function(data) {
      for(var i = 0; i < data.messages.length; i++) {
        var curLoc = data.messages[i];
        planeTracker.lastLocation = curLoc;
        planeTracker.drawRect(curLoc.x, curLoc.y, 20, 20);
      }
    });
  },
  
  poll_for_updates: function() {
    var planeTracker = this;
    var myImage = new Image();
    myImage.src = planeTracker.baseImage;
    myImage.onload = function() {
      planeTracker.context.drawImage(myImage, 0, 0);
      planeTracker.get_last_location();
      setTimeout(function() {
        if(planeTracker.should_run) planeTracker.poll_for_updates();
      }, 2500);
    };
  }
};
