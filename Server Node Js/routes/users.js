var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res) {
  res.send('respond with a resource%');
  var url=require('url');
  console.log(url.parse( req.url,true).query);
});

module.exports = router;
