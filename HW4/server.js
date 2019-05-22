const express = require('express')
const request = require('request');
const https = require('https');
var cors = require('cors');
const app = express()

var bodyParser = require("body-parser");
app.use(cors());
app.use(express.static('public'));
app.use(bodyParser.urlencoded({ extended: false }));

var appId = "{your_key}";

app.get('/searchResults', function (req, res) {
    try {
        var endpoint = "http://svcs.ebay.com/services/search/FindingService/v1";
        var responseEncoding = "JSON";
        var apicall = endpoint + "?OPERATION-NAME=findItemsAdvanced" + "&SERVICE-VERSION=1.0.0" + "&paginationInput.entriesPerPage=50&SECURITY-APPNAME=" + appId + "&RESPONSE-DATA-FORMAT=" + responseEncoding + "&REST-PAYLOAD";
        if (req.query.product_name) {
            apicall += "&keywords=" + req.query.product_name;
        }
        if (req.query.category && req.query.category != 0) {
            apicall += "&category=" + req.query.category;
        }
        var itemfil = -1;
        itemfil = itemfil + 1;
        apicall += "&itemFilter(" + itemfil + ").name=HideDuplicateItems";
        apicall += "&itemFilter(" + itemfil + ").value=true";
        if (req.query.new || req.query.used || req.query.unspecified) {
            itemfil = itemfil + 1;
            var i = 0;
            apicall += "&itemFilter(" + itemfil + ").name=Condition";
            if (req.query.new) {
                apicall += "&itemFilter(" + itemfil + ").value(" + i + ")=New";
            }
            if (req.query.used) {
                i += 1;
                apicall += "&itemFilter(" + itemfil + ").value(" + i + ")=Used";
            }
            if (req.query.unspecified) {
                i += 1;
                apicall += "&itemFilter(" + itemfil + ").value(" + i + ")=Unspecified";
            }
        }
        if (req.query.localpickup) {
            itemfil = itemfil + 1;
            apicall += "&itemFilter(" + itemfil + ").name=LocalPickupOnly&itemFilter(" + itemfil + ").value=true";
        }
        if (req.query.freeshipping) {
            itemfil = itemfil + 1;
            apicall += "&itemFilter(" + itemfil + ").name=FreeShippingOnly&itemFilter(" + itemfil + ").value=true";
        }
        var miles = 10;
        if (req.query.miles) {
            miles = req.query.miles;
        }
        itemfil = itemfil + 1;
        apicall += "&itemFilter(" + itemfil + ").name=MaxDistance&itemFilter(" + itemfil + ").value=" + miles;
        if (req.query.dis) {
            if (req.query.dis == "here") {
                itemfil = itemfil + 1;
                apicall += "&buyerPostalCode=" + req.query.zipcode_here;
            } else if (req.query.dis == "zip") {
                itemfil = itemfil + 1;
                apicall += "&buyerPostalCode=" + req.query.zipcode;
            }
        }
        apicall += "&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";
        //console.log("APICALL" + apicall);
        request(apicall, { json: true }, (err, resp, body) => {
            //var itemsArr = [];
            if (err) { return console.log(err); }
            var itemsArr = [];
            if (body.findItemsAdvancedResponse[0].ack == "Success") {
                if (body.findItemsAdvancedResponse[0].searchResult) {
                    if (body.findItemsAdvancedResponse[0].searchResult[0].item) {
                        var items = body.findItemsAdvancedResponse[0].searchResult[0].item;
                        var i = 0;
                        items.forEach(function(item) {
                            i += 1;
                            var index = i;
                            var itemId = item.itemId[0];
                            var photo = "N/A";
                            if (item.galleryURL && item.galleryURL.length > 0) {
                                photo = item.galleryURL[0];
                            } else {
                                photo = "N/A";
                            }
                            var name = "N/A";
                            if (item.title && item.title.length > 0) {
                                name = item.title[0];
                            }
                            //name = name.replace('"','');
                            var price = "N/A";
                            if (item.sellingStatus) {
                                price = item.sellingStatus[0].currentPrice[0].__value__;
                            } else {
                                price = "N/A";
                            }
                            var ship = -1;
                            var ship_option = "N/A";
                            var shipping_tab = {};
                            if (item.shippingInfo && item.shippingInfo.length > 0) {
                                if (item.shippingInfo[0].shippingServiceCost) {
                                    if (item.shippingInfo[0].shippingServiceCost[0].__value__) {
                                        ship = item.shippingInfo[0].shippingServiceCost[0].__value__;
                                    }
                                }
                                var ship_to_locations = "N/A";
                                if (item.shippingInfo[0].shipToLocations && item.shippingInfo[0].shipToLocations.length > 0) {
                                    ship_to_locations = item.shippingInfo[0].shipToLocations;
                                } else {
                                    ship_to_locations = "N/A";
                                }
                                var handling_time = "N/A";
                                if (item.shippingInfo[0].handlingTime && item.shippingInfo[0].handlingTime.length > 0) {
                                    handling_time = item.shippingInfo[0].handlingTime[0];
                                    if (handling_time == "0" || handling_time == "1") {
                                        handling_time += " Day";
                                    } else {
                                        handling_time += " Days";
                                    }
                                } else {
                                    handling_time = "N/A";
                                }
                                var expedited_shipping = "N/A";
                                if (item.shippingInfo[0].expeditedShipping && item.shippingInfo[0].expeditedShipping.length > 0) {
                                    expedited_shipping = item.shippingInfo[0].expeditedShipping[0];
                                } else {
                                    expedited_shipping = "N/A";
                                }
                                var oneDayShippingAvailable = "N/A";
                                if (item.shippingInfo[0].oneDayShippingAvailable && item.shippingInfo[0].oneDayShippingAvailable.length > 0) {
                                    oneDayShippingAvailable = item.shippingInfo[0].oneDayShippingAvailable[0];
                                } else {
                                    oneDayShippingAvailable = "N/A";
                                }
                                if (ship == -1) {
                                    ship_option = "N/A";
                                } else if (ship == 0.00) {
                                    ship_option = "Free Shipping";
                                } else {
                                    ship_option = "$" + ship;
                                }
                                shipping_tab["shipping_cost"] = ship_option;
                                shipping_tab["shipping_locations"] = ship_to_locations;
                                shipping_tab["handling_time"] = handling_time;
                                shipping_tab["expedited_shipping"] = expedited_shipping;
                                shipping_tab["oneDayShippingAvailable"] = oneDayShippingAvailable;
                            }

                            if (item.returnsAccepted) {
                                shipping_tab["returns_accepted"] = item.returnsAccepted[0];
                            }

                            var zipcode = "N/A";
                            if (item.postalCode && item.postalCode.length > 0) {
                                zipcode = item.postalCode[0];
                            } else {
                                zipcode = "N/A";
                            }
                            var sellerName = "N/A";
                            if (item.sellerInfo && item.sellerInfo.length > 0) {
                                slrname = item.sellerInfo[0].sellerUserName[0];
                                sellerName = slrname.toUpperCase();
                            }
                            itemsArr.push({"index" : index, "itemId" : itemId, "image" : photo, "title" : name, "price" : price, "shipping" : ship_option, "zipcode" : zipcode, "sellerName" : sellerName, "shipping_tab" : shipping_tab});
                        });
                    }
                }
            }
            res.setHeader('Content-Type', 'application/json');
            res.send(itemsArr);
        });
    }
    catch(error) {
        var emptyResp = [];
        res.setHeader('Content-Type', 'application/json');
        res.send(emptyResp);
    }

});

app.get('/getDetails', function (req, res) {
    try {
        var endpoint = "http://open.api.ebay.com/shopping";
        var responseEncoding = "JSON";
        var apicall = endpoint + "?callname=GetSingleItem" + "&siteid=0" + "&version=967&appid=" + appId + "&responseencoding=" + responseEncoding;
        if (req.query.itemId) {
            apicall += "&ItemID=" + req.query.itemId;
        }
        apicall += "&IncludeSelector=Description,Details,ItemSpecifics";
        //console.log("APICALL" + apicall);
        request(apicall, { json: true }, (err, resp, body) => {
            if (err) { return console.log(err); }
            if (body.Ack == "Success") {
                if (body.Item) {
                    var itemDetails = body.Item;
                    // Get product tab details
                    var pictureURL = [];
                    if (itemDetails.PictureURL && itemDetails.PictureURL.length > 0) {
                        pictureURL = itemDetails.PictureURL;
                    }
                    var title = "N/A";
                    if (itemDetails.Title) {
                        title = itemDetails.Title;
                        //replace double quotes
                    } else {
                        title = "N/A";
                    }
                    var subtitle = "N/A";
                    if (itemDetails.Subtitle) {
                        subtitle = itemDetails.Subtitle;
                    } else {
                        subtitle = "N/A";
                    }
                    var price = "N/A";
                    if (itemDetails.CurrentPrice) {
                        if (itemDetails.CurrentPrice.Value) {
                            price = "$" + itemDetails.CurrentPrice.Value;
                        }
                    } else {
                        price = "N/A";
                    }
                    var location = "";
                    if (itemDetails.Location) {
                        location = itemDetails.Location;
                    } else {
                        location = "N/A";
                    }
                    var returnpolicy = "N/A";
                    if (itemDetails.ReturnPolicy) {
                        if (itemDetails.ReturnPolicy.ReturnsAccepted) {
                            returnpolicy = itemDetails.ReturnPolicy.ReturnsAccepted;
                        } else {
                            returnpolicy = "N/A";
                        }
                        if (itemDetails.ReturnPolicy.ReturnsWithin && returnpolicy != "N/A") {
                            returnpolicy += " within " + itemDetails.ReturnPolicy.ReturnsWithin;
                        } else {
                            returnpolicy = "N/A";
                        }
                    }
                    var itemspecifics = {};
                    if (itemDetails.ItemSpecifics) {
                        if (itemDetails.ItemSpecifics.NameValueList) {
                            var nameValList = itemDetails.ItemSpecifics.NameValueList;
                            nameValList.forEach(function(nv) {
                                itemspecifics[nv.Name.toString()] = nv.Value.toString();
                            });
                        }
                    }


                    // Get photos tab details
                    // Google API

                    // Get shipping tab details
                    // From previous page /searchResults

                    // Get seller tab details
                    var feedback_score = "N/A";
                    var popularity = "N/A";
                    var feedback_rating_star = "N/A";
                    var toprated = "N/A";
                    var storename = "N/A";
                    var buy_product_at = "N/A";
                    var seller_name = "N/A";
                    if (itemDetails.Seller) {
                        if (itemDetails.Seller.UserID) {
                            slrname = itemDetails.Seller.UserID;
                            seller_name = slrname.toUpperCase();
                        }
                        if (itemDetails.Seller.FeedbackScore) {
                            feedback_score = itemDetails.Seller.FeedbackScore;
                        } else {
                            feedback_score = "N/A";
                        }
                        if (itemDetails.Seller.PositiveFeedbackPercent) {
                            popularity = itemDetails.Seller.PositiveFeedbackPercent;
                        } else {
                            popularity = "N/A";
                        }
                        if (itemDetails.Seller.FeedbackRatingStar) {
                            feedback_rating_star = itemDetails.Seller.FeedbackRatingStar;
                        } else {
                            feedback_rating_star = "N/A";
                        }
                        if (itemDetails.Seller.TopRatedSeller) {
                            toprated = itemDetails.Seller.TopRatedSeller;
                        } else {
                            toprated = "N/A";
                        }
                    }
                    if (itemDetails.Storefront) {
                        if (itemDetails.Storefront.StoreName) {
                            storename = itemDetails.Storefront.StoreName;
                        } else {
                            storename = "N/A";
                        }
                        if (itemDetails.Storefront.StoreURL) {
                            buy_product_at = itemDetails.Storefront.StoreURL;
                        } else {
                            buy_product_at = "N/A";
                        }
                    }
                    var ViewItemURLForNaturalSearch
                    if (itemDetails.ViewItemURLForNaturalSearch) {
                        ViewItemURLForNaturalSearch = itemDetails.ViewItemURLForNaturalSearch;
                    } else {
                        ViewItemURLForNaturalSearch = "N/A"
                    }
                    var sellerTab = {"seller_name" : seller_name, "feedback_score" : feedback_score, "popularity" : popularity, "feedback_rating_star" : feedback_rating_star, "toprated" : toprated, "storename" : storename, "buy_product_at" : buy_product_at};

                    var productDetails = {"Photo" : pictureURL, "Title" : title, "Subtitle" : subtitle, "Price" : price, "Location" : location, "Return_Policy" : returnpolicy, "ItemSpecifics" : itemspecifics, "seller_tab" : sellerTab, "facebook_link": ViewItemURLForNaturalSearch};

                    var googleapicall = "https://www.googleapis.com/customsearch/v1?";
                    googleapicall += "q=" + title + "&cx={your_key}:s7je5h48oqc&imgSize=huge&imgType=news&num=8&searchType=image&key={your_key}";

                    //console.log("GOOGLE" + googleapicall);

                    var encodedapicall = encodeURI(googleapicall);
                    var photosList = {};
                    request(encodedapicall, { json: true }, (err, resp, body) => {
                        if (err) { return console.log(err); }
                        var counter = 1;
                        //console.log(body);
                        if (body) {
                            if (body.items) {
                                body.items.forEach(function(item) {
                                    photosList[counter] = item.link;
                                    counter ++;
                                });
                            }
                        }
                    });
                    var similarItemsList = [];
                    var endpoint = "https://svcs.ebay.com/MerchandisingService";
                    var responseEncoding = "JSON";
                    var similarItemsCall = endpoint + "?OPERATION-NAME=getSimilarItems" + "&SERVICE-NAME=MerchandisingService" + "&SERVICEVERSION=1.1.0&CONSUMER-ID=" + appId + "&RESPONSE-DATA-FORMAT=" + responseEncoding + "&REST-PAYLOAD&maxResults=20";
                    if (req.query.itemId) {
                        similarItemsCall += "&itemId=" + req.query.itemId;
                    }
                    //console.log(similarItemsCall);
                    /*request(similarItemsCall, { json: true }, (err, resp, body) => {
                        console.log(body);
                    });*/
                    https.get(similarItemsCall, (resp) => {
                      let data = '';

                      // A chunk of data has been recieved.
                      resp.on('data', (chunk) => {
                        data += chunk;
                      });

                      // The whole response has been received. Print out the result.
                      resp.on('end', () => {
                          var similarItemsResponse = JSON.parse(data).getSimilarItemsResponse;
                          if(similarItemsResponse.itemRecommendations) {
                              if (similarItemsResponse.itemRecommendations.item && similarItemsResponse.itemRecommendations.item.length > 0) {
                                  var items = similarItemsResponse.itemRecommendations.item;
                                  items.forEach(function(item) {
                                      var itemId;
                                      if (item.itemId) {
                                          itemId = item.itemId;
                                      }
                                      var title;
                                      if (item.title) {
                                          title = item.title;
                                      } else {
                                          title = "N/A";
                                      }
                                      //replace double quotes in title
                                      var photo;
                                      if (item.imageURL) {
                                          photo = item.imageURL;
                                      } else {
                                          photo = "http://pics.ebaystatic.com/aw/pics/express/icons/iconPlaceholder_96x96.gif";
                                      }
                                      var price;
                                      if (item.buyItNowPrice) {
                                          price = parseFloat(item.buyItNowPrice.__value__);
                                      }
                                      if (price == 0) {
                                          if (item.currentPrice) {
                                              price = parseFloat(item.currentPrice.__value__);
                                          }
                                      }
                                      var shippingCost;
                                      if (item.shippingCost) {
                                          shippingCost = parseFloat(item.shippingCost.__value__);
                                      }
                                      var daysLeft;
                                      if (item.timeLeft) {
                                          var timeLeft = item.timeLeft;
                                          var first = "P";
                                          var second = "D";
                                          daysLeft = parseFloat(timeLeft.match(new RegExp(first + "(.*)" + second))[1]);
                                      }
                                      var viewItemURL;
                                      if (item.viewItemURL) {
                                          viewItemURL = item.viewItemURL;
                                      }
                                      similarItemsList.push({"photo" : photo, "title" : title, "price" : price, "shipping_cost" : shippingCost, "days_left" : daysLeft, "view_item_url" : viewItemURL});
                                  });
                              }
                          }
                      });

                    }).on("error", (err) => {
                      console.log("Error: " + err.message);
                    });

                    setTimeout(function() {
                        productDetails["google_photos"] = photosList;
                        productDetails["similar_items"] = similarItemsList;
                        res.setHeader('Content-Type', 'application/json');
                        var resultJson = {"product_details" : productDetails};
                        res.setHeader('Content-Type', 'application/json');
                        res.send(resultJson);
                    }, 1000);
                }
            }
            /*res.setHeader('Content-Type', 'application/json');
            res.send(productDetails);*/
        });
    }
    catch(error) {
        var emptyJson = {};
        res.send(emptyJson);
    }
});

app.get('/getZipCodes', function (req, res) {
    var endpoint = "http://api.geonames.org/postalCodeSearchJSON?";
    var responseEncoding = "JSON";
    var apicall = endpoint;
    if (req.query.postalCode) {
        apicall += "&postalcode_startsWith=" + req.query.postalCode;
    }
    apicall += "&username=murrali&country=US&maxRows=5";
    //console.log("APICALL" + apicall);
    request(apicall, { json: true }, (err, resp, body) => {
        var postalCodeArr = [];
        if (body.postalCodes) {
            body.postalCodes.forEach(function(code) {
                postalCodeArr.push(code.postalCode);
            });
        }
        res.setHeader('Content-Type', 'application/json');
        res.send(postalCodeArr);
    });
});

app.listen(8081, () => console.log('Example app listening on port 8081!'));
