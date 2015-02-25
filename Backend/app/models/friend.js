var mongoose = require('mongoose');

var friendSchema = mongoose.Schema({
    
        user1       	: String,
        user2	        : String
     
});

// create the model for friends and expose it to our app
module.exports = mongoose.model('Friend', friendSchema);