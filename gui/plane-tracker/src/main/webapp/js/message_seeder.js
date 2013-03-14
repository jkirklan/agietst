/*
 * message_seeder.js - Class to use for seeding messages to use with the UI
 */

function MessageSeeder(resultContainer) {
  this.baseServiceUrl = "/agie-services/rest/agie-test-poller/pushmsg";
  this.resultContainer = resultContainer;
}
MessageSeeder.prototype = {
  sendMessage: function(x, y) {
    var loc = {x: x, y: y};
    var msgSender = this;
    $.ajax(this.baseServiceUrl, {
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(loc)
    })
    .done(function(data) {
      msgSender.resultContainer.append("<li>" + data.message + "</li>");
    });
  }
};