
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("newBid",function(request,response){
  
  var Bids = Parse.Object.extend("Bids");
  // var User = Parse.Object.extend("User");
  var HireData = Parse.Object.extend("HireData");
  // var Installation = Parse.Object.extend("Installation");

  var bidQuery = new Parse.Query(Bids);
  var userQuery = new Parse.Query(Parse.User);
  var projectQuery = new Parse.Query(HireData);
  var pushQuery = new Parse.Query(Parse.Installation);

  ProjectId   = request.params.ProjectId;
  BidderName  = request.params.BidderName;
  Bid         = request.params.Bids;

  var topic;
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
                info     =    result[0];
                empObject=    info;
                employer =    info.get("username").toString();
                topic    =    info.get("topic").toString(); 
                category =    info.get("category").toString();
                console.log("Got employer details from HireData");
          }
      },
      error:function(){
          response.error("Oops !! Could not connect with server");
      }
  });
  
  Parse.Cloud.useMasterKey();
  // pushQuery.equalTo("username",employer);
  pushQuery.include("user");
  pushQuery.equalTo("user",empObject);

  pushQuery.find({
      success:function(result){
          if(result.length==0){
                console.log("Installation not found for "+employer);
          }
          else{
                console.log("Installation id is "+result[0].objectId.toString());
          }
      },
      error:function(error){
          console.log(error);
      }
  });


  Parse.Cloud.useMasterKey();
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