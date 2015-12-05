
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("newBid",function(request,response){
  
  var Bids = Parse.Object.extend("Bids");
  var User = Parse.Object.extend("User");
  var HireData = Parse.Object.extend("HireData");
  var Installation = Parse.Object.extend("Installation");

  var bidQuery = new Parse.Query(Bids);
  var userQuery = new Parse.Query(User);
  var projectQuery = new Parse.Query(HireData);
  var pushQuery = new Parse.Query(Installation);

  ProjectId   = request.params.ProjectId;
  BidderName  = request.params.BidderName;
  Bid         = request.params.Bids;

  var topic = request.params.Topic;
  var employer;
  var category;
  var userid;
  var empObject;

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

  userQuery.equalTo("username",employer);
  userQuery.find({
      success:function(result){
        console.log("inside employer find for "+employer);
          if(result.length!=1){
            console.log("employer fishy, length is "+ result.length);       
          }
          else{
            info = result[0];
            userid = info.get("objectId");
            console.log(id);
            empObject = result[0];
          }
        },
    
      error:function(error){
          console.log(error);
    }
  });

  var query = new Parse.Query("User");
  query.equalTo('username', employer);
  pushQuery.matchesQuery('user', query);
  Parse.Push.send({
        where: pushQuery,
        data: {
          alert: "New Bidder for your Post "+request.params.Topic+" : "+BidderName,
        }
      },
      {
        success:function(){
          response.success("Notification sent to "+employer);
          console.log("Notification sent");
        },
        error:function(error){
          response.error("Oops !! Cannot send Notification to "+employer);
        }
  });

});