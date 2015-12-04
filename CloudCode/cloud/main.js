
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("newBid",function(request,response){
  
  var User = Parse.Object.extend("User");
  var Installation = Parse.Object.extend("Installation");
  var Bids = Parse.Object.extend("Bids");
  var HireData = Parse.Object.extend("HireData");
  
  var bidQuery = new Parse.Query(Bids);
  var userQuery = new Parse.Query(User);
  var projectQuery = new Parse.Query(HireData);
  var pushQuery = new Parse.Query(Installation);

  ProjectId   = request.params.ProjectId;
  BidderName  = request.params.BidderName;
  Bid         = request.params.Bids;

  // var topic = request.params.Topic;
  var employer;
  var category;

  projectQuery.equalTo("objectId",ProjectId);
  projectQuery.find({
      success:function(result){
        if(result.length!=1){
                response.success("something is fishy");
        }
        else{
              info = result[0];
              employer = info.get("username");
              topic = info.get("topic"); 
              category = info.get("category");
        }
      },
      error:function(){
        response.error("Oops !! Could not connect with server");
      }
  });

  pushQuery.equalTo("username",employer);
  Parse.Push.send({
      where: pushQuery,
      data: {
        alert: "New Bidder for your project "+request.params.Topic+" : "+BidderName,
      }
    },{
        success:function(){
          response.success("Notification sent to "+employer);
        },
        error:function(error){
          response.error("Oops !! Cannot send Notification to "+employer);
        }
  });

});