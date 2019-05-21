<html lang="en" dir="ltr">
   <head>
        <meta charset="utf-8">
        <title></title>
        <script>
             function resizeIframe(obj) {
                var height = obj.contentWindow.document.body.scrollHeight;
                height = height + 100;
                obj.style.height = height + 'px';
                return false;
            }
 
			function enableNearBy() {
                if(document.getElementById("nearbyName").checked) {
                    document.getElementById("nearby").style.pointerEvents  = "auto";
					document.getElementById("zipcode").style.pointerEvents  = "none";			
                    document.getElementById("nearby").style.opacity  = "1";
					document.getElementById("here").disabled=false;
					document.getElementById("zip").disabled=false;
					document.getElementById("miles_id").disabled=false;
                    getCurrentLocation();
                } else {
                    document.getElementById("nearby").style.pointerEvents  = "none";
					document.getElementById("miles_id").style.placeholder="10";
					document.getElementById("miles_id").value="";
                    document.getElementById("nearby").style.opacity  = "0.3";
					document.getElementById("here").checked=true;
					document.getElementById("zip").checked=false;
					document.getElementById("zipcode").style.placeholder="zip code";
					document.getElementById("zipcode").value= "";
                    document.getElementById("zipcode").required = false;
					document.getElementById("zip").disabled=true;
					document.getElementById("zipcode").disabled=true;
					document.getElementById("here").disabled=true;
					document.getElementById("miles_id").disabled=true;
                }
            }           
			
            function changeZipReq() {
                if(document.getElementById("zip").checked) {
                    document.getElementById("zipcode").required = true;
                }else {
                    document.getElementById("zipcode").required = false;
                }
            }
            
            function parseJSONResults(jsonObj) {
                innerHTML = "<table> <tr> <th> Index </th><th> Photo </th>  <th> <b> Name </b> </th>  <th> <b> Price </b> </th>  <th style='padding-right:5px;'> <b> Zip code </b> </th>  <th> <b> Condition </b> </th>  <th style='padding-right:5px;'> <b> Shipping Option </b> </th> </tr>" ;
                data = jsonObj;
                postData = jsonObj[data.length - 1].POST_DATA;
                postDataKeys = Object.keys(postData);
                for (j = 0; j < data.length - 1; j++) {
                    innerHTML += "<tr>";
                    innerHTML += "<td style='text-align:left'> " + data[j].Index + "</td>";
					if(data[j].PHOTO != "N/A") {
						innerHTML += "<td> <img style='padding:5px 5px 5px 5px;' height = '75px' width ='75px' src = '" + data[j].PHOTO + "'/></td>";
					} else {
					      innerHTML += "<td style='text-align:left'> N/A </td>";
					}
                    innerHTML += "<td style='text-align:left;padding-right:50px;'><form action='' method='post'> <a href='javascript:' onclick='parentNode.submit();' >" +   data[j].NAME + "</a>";
                    innerHTML += '<input type="hidden" name="details" value=' + data[j].ID  + '>';
                    for (i = 0; i < postDataKeys.length; i++) {
                        if(postDataKeys[i] != "submit" && postDataKeys[i] != "details") {
                            innerHTML += '<input type="hidden" name="' + postDataKeys[i]+ '" value="' + postData[postDataKeys[i]]  + '">';
                        }
                    }
                    innerHTML += '</form> </td>';
                    innerHTML += "<td style='text-align:left;padding-right:5px;'> " + data[j].PRICE + "</td>";
                    innerHTML += "<td style='text-align:left;padding-right:10px;'> " + data[j].ZIPCODE + "</td>";
                    innerHTML += "<td style='text-align:left;padding-right:5px;'> " + data[j].CONDITION + "</td>";
                    innerHTML += "<td style='text-align:left;padding-right:5px;'> " + data[j].SHIPPING_INFO + "</td>";
                    innerHTML += "</tr>";
                    
                }
                document.getElementById("result").innerHTML = innerHTML
                document.getElementById("display_website").style.display = "none";
                document.getElementById("display_similar").style.display = "none";
                return false;
            }
            
            htmlContent = "";
            var similar_items;
            var post_data;
            
            function parseJSONDetails(jsonObj) {
                data = jsonObj;
                innerHTML = "<div style='font-size: 40px'> <b>Item Details</b> </div><table>" ;
                keys = Object.keys(data);
                post_data = data["POST_DATA"];
                for (j = 0; j < keys.length; j++) {
                    if(keys[j] != "description") {
                        if(keys[j] == "Photo" && data[keys[j]] != "N/A" ) {
                            innerHTML += "<tr><td style='padding-left:10px; padding-right:40px'><b>" + keys[j] + "</b></td><td  style='padding-left:10px;padding-right:10px'><img src ='" + data[keys[j]] + "' height ='275px' width ='275px' /></td></tr>";
                        } else if(keys[j] == 'similar') {
                            similar_items = data[keys[j]];
                        } else if (keys[j] == 'POST_DATA') {
							//do ntg..
						} else if(keys[j] == 'No Detail Info from seller') {
						         //innerHTML += "<tr><td style='padding-left:10px;padding-right:40px'><b>" + keys[j] + "</b></td><td  style='padding-left:10px;padding-right:10px;' bgcolor = '#d9d9d9'></td></tr>";						}
                        } else {
							if(data[keys[j]] != "N/A") {
								innerHTML += "<tr><td style='padding-left:10px;padding-right:40px'><b>" + keys[j] + "</b></td><td  style='padding-left:10px;padding-right:10px'>" +  data[keys[j]] + "</td></tr>";
							}
                        }
                    } else {
							htmlContent= data[keys[j]];
                    }   
                }
                innerHTML += "</table>";
                document.getElementById("result").innerHTML = innerHTML;
                document.getElementById("display_website").style.display = "initial";
                document.getElementById("display_similar").style.display = "initial";
                return false;
            }
            
            
            
            function displayHTML() {
				hideSimilar();
                document.getElementById("display_text").innerHTML = "click to hide seller message";
                document.getElementById("display_image").innerHTML = "<a href='#' onclick='hideHTML(); return false;'><img src ='http://csci571.com/hw/hw6/images/arrow_up.png' height ='20px' width='40px'></a>";
				if (htmlContent != "N/A") {
					var iframe = document.createElement('iframe');
					iframe.seamless = true ;
					iframe.style.border = 0;
					iframe.scrolling= "no";
					iframe.addEventListener("load", function() {
									this.style.height = (this.contentWindow.document.body.scrollHeight + 20) + 'px';
					});
					iframe.setAttribute("srcdoc", htmlContent);
					document.getElementById("website").appendChild(iframe);
				} else {
					displayMessage = "<div style='padding-bottom:10px;'> <div style='padding-top:0px; border:3px solid #cccccc;padding-bottom:0px; font-size:16px;block:inline;background-color:#d9d9d9;'><b> No Seller Message found.</b></div></div>";
					document.getElementById("website").innerHTML = displayMessage;
				}
                return false;
            }
            
            function hideHTML() {
                document.getElementById("display_text").innerHTML = "click to show seller message";
                document.getElementById("display_image").innerHTML = "<a href='#' onclick='displayHTML(); return false;'><img src ='http://csci571.com/hw/hw6/images/arrow_down.png' height ='20px' width='40px'></a>"
                document.getElementById("website").innerHTML = "";
                return false;
            }
            
        
            function getCurrentLocation() {
                var req = new XMLHttpRequest();
                req.open("GET", "http://ip-api.com/json", false);
                req.send();
				if(req.status == 200) {
					var response = JSON.parse(req.responseText);
					zipcode = response.zip;
					document.getElementById("here").value = zipcode;
					document.getElementById("form_submit").disabled = false;
				}
            }
            
            function displaySimilar() {
                    hideHTML();
                    document.getElementById("display_similar_text").innerHTML = "click to hide similar items";
                    document.getElementById("display_similar_image").innerHTML = "<a href='#' onclick='hideSimilar(); return false;'><img src ='http://csci571.com/hw/hw6/images/arrow_up.png' height ='20px' width='40px'></a>";
					if(similar_items != "N/A") {
						data = similar_items;
						document.getElementById("similar").style.width = "1200px";
						document.getElementById("similar").style.paddingBottom = "10px";
						document.getElementById("similar").style.height = "auto";
						document.getElementById("similar").style.overflowX  = "scroll";
						document.getElementById("similar").style.overflowY  = "hidden";
						document.getElementById("similar").style.whiteSpace = "nowrap";
						document.getElementById("similar").style.border = "1px solid #c2c2d6";

						postDataKeys = Object.keys(post_data);
						for (j = 0; j < data.length; j++) {
							innerHTML1 = "";
							var div = document.createElement("div");
							div.id="content_similar"
							div.style.width = "150px";
							div.style.height = "auto";
							div.style.padding = "0px 50px 0px 50px";
							div.style.display = "inline-block";
							div.style.verticalAlign ="middle";
							innerHTML1 += "<img style='padding-top:10px; padding-right:10px' height = '150px' width = '150px' src = '" + data[j].PHOTO + "'/><br>";
							innerHTML1 += "<form action='' method='post'> <a href='javascript:' onclick='parentNode.submit();' > <p style ='width :150px;padding-top:10px;white-space:pre-wrap;word-wrap:break-word;'>" +   data[j].NAME + "</p></a>";
							innerHTML1 += '<input type="hidden" name="details" value="' + data[j].ITEMID  + '">';
							for (i = 0; i < postDataKeys.length; i++) {
								if(postDataKeys[i] != "submit" && postDataKeys[i] != "details") {
									innerHTML1 += '<input type="hidden" name=" ' + postDataKeys[i]+ '" value="' + post_data[postDataKeys[i]]  + '"	>';
								}
							}
							innerHTML1 += "</form>"
							innerHTML1 += "<div style='bottom: 0;'><br><b>$" + data[j].PRICE + "</b><br></div></div> ";    
							div.innerHTML = innerHTML1;
							document.getElementById("similar").appendChild(div);
						}
						document.getElementById("end").style.height = "20px";
					} else {
						document.getElementById("similar").style.width = "1200px";
						document.getElementById("similar").style.padding = "10px 0px 10px 10px";
						document.getElementById("similar").style.height = "auto";
						document.getElementById("similar").style.overflowX  = "scroll";
						document.getElementById("similar").style.overflowY  = "hidden";
						document.getElementById("similar").style.whiteSpace = "nowrap";
						document.getElementById("similar").style.border = "1px solid #c2c2d6";
						var div = document.createElement("div");
						div.id="content_similar";
						div.style.width = "1100px";
						div.style.height = "auto";
						div.style.fontSize = "20px";
						div.style.padding = "0px 50px 0px 50px";
						div.style.border = "1px solid #c2c2d6";
						div.style.display = "inline-block";
						div.style.verticalAlign ="middle";
						var value = "No Similar Item found.";
						div.innerHTML = value.bold();
						document.getElementById("similar").appendChild(div);					
					}
                    return false;
            }
            
            function hideSimilar() {
                document.getElementById("display_similar_text").innerHTML = "click to show similar items";
                document.getElementById("display_similar_image").innerHTML = "<a href='#' onclick='displaySimilar(); return false;'><img src ='http://csci571.com/hw/hw6/images/arrow_down.png' height ='20px' width='40px'></a>"
                document.getElementById("similar").innerHTML = "";
                
                document.getElementById("similar").style.width = "0";
                document.getElementById("similar").style.height = "0";
                document.getElementById("similar").style.padding = "0px";
                document.getElementById("similar").style.overflow  = "scroll";
                document.getElementById("end").style.height = "0";
				document.getElementById("similar").style.border = "0px solid #c2c2d6";
                return false;
            }
            
            function resetAllFields() {
                document.getElementById("result").innerHTML = "";
                document.getElementById("result").style.height = 0;
                document.getElementById("display_website").innerHTML = "";
                document.getElementById("display_website").style.height = 0;
                document.getElementById("website").innerHTML = "";
                document.getElementById("website").style.height = 0;
                document.getElementById("similar").innerHTML = "";
                document.getElementById("similar").style.height = 0;
                document.getElementById("similar").style.paddingBottom = 0;
                document.getElementById("similar").style.border = "0px solid #c2c2d6";
				document.getElementById("similar").style.padding = "0px";
                document.getElementById("display_similar").innerHTML = "";
                document.getElementById("display_website").style.height = 0;
                
                document.getElementById("keyword_id").value = "";
                
                document.getElementById("all_id").selected = true;
                
                document.getElementById("new_id").checked = false;
                document.getElementById("used_id").checked = false;
                document.getElementById("unspecified_id").checked = false;
                
                document.getElementById("localpickup_id").checked = false;
                document.getElementById("freeshipping_id").checked = false;
                
                document.getElementById("nearbyName").checked = false;

                
                document.getElementById("miles_id").placeholder="10";
				document.getElementById("miles_id").value="";
                document.getElementById("here").checked = true;
                document.getElementById("zip").checked = false;
                document.getElementById("zipcode").value="";
                
                document.getElementById("nearby").style.pointerEvents  = "none";
                document.getElementById("nearby").style.opacity  = "0.3";
				
				document.getElementById("zip").disabled=true;
				document.getElementById("zipcode").disabled=true;
				document.getElementById("here").disabled=true;
				document.getElementById("miles_id").disabled=true;

            }
            
            function displayErrorMessage(message) {
                if(message.includes("Invalid postal code")) {
                    message = "Zipcode is invalid";
                }
				if(message.includes("Negative value is not allowed for item filter, MAX_DISTANCE")) {
                    message = "Negative value is not allowed for distance";
                }
				if(message.includes("Invalid numeric value.")) {
                    message = "Only numbers are allowed for distance field";
                }
				if(message.includes("Invalid keyword.")) {
                    message = "Keyword should be greater than 1 character";
                }
                innerHTMLError = "<div style=''> <div style='width:700px;padding-top:0px; border:3px solid #cccccc;padding-bottom:0px; font-size:16px;block:inline;background-color:#d9d9d9;'>" + message + "</div></div>";
                document.getElementById("result").innerHTML = innerHTMLError;
            }
			
			function clearZipCode() {
				document.getElementById("zipcode").value = "";
				document.getElementById("zipcode").placeholder = "zip code";
				document.getElementById("zipcode").style.pointerEvents  = "none";
				document.getElementById("zipcode").disabled=true;
			}
			
			function enablePointerEvent() {
				document.getElementById("zipcode").style.pointerEvents  = "auto";
				document.getElementById("zipcode").disabled=false;
			}
            
        </script>
        <style>
            html, body {
                width:100%;
                height:100%;
            }
            #form_elements {
                font-size:20px;
            }
            
            #underline {
                  width:400px;
                  text-decoration: underline;
                  margin-left:50px;
                  margin-right:50px;
            }
            
            #form_align {
                padding-left:20px;
                padding-top:5px;
                padding-bottom: 5px;
            }
            
            #shipping {
                padding-left:37px;
                padding-right:0px;
                display: inline;
            }
            
            #condition {
                padding-left:13px;
                display: inline;
            }
            
            label {
                font-weight:bold;
            }
        
            #mile {
                display:inline;
                padding-left:2px;
				position:absolute;
			}
            
            #nearby {
                opacity: .3;
                display:inline;
                pointer-events:none;
            }
            #title {
                text-align:center;
                width:570px;
                padding-top:5px;
                border-bottom:2px solid #c2c2d6 ;
            }
            #result {
                padding-top:30px;
            }


            #result table, #result th, #result td {
                border-collapse:collapse;
                border: 2px solid #c2c2d6;
            }
            
            #website {
                position:relative;
                padding-left:200px;
                padding-right:200px;
                padding-top:10px;
                height:auto;
            }
            #display_website {
                display:none;
                padding-top:20px;
            }
            
            #display_text{
                padding-top:30px;
                color:#a3a3c2;
                font-size:16px;
                font-weight: 90;
            }
            
            #display_image{
                padding-top:10px;
                object-fit: cover;
                height:35px;
                width:40px;
            }
            
            #display_similar_text{
                color:#a3a3c2;
                font-size:16px;
                font-weight: 90;
            }
            
            #display_similar_image{
                padding-top:10px;
                object-fit: cover;
                height:35px;
                width:40px;
            }
            
            a, a:visited, a:hover, a:active {
                text-decoration: inherit;
            }
            a {
                color:inherit;
            }
            a:hover, a:active {
                color:#c2c2d6;
            }
            
            #display_similar {
                display:none;
                padding-top:20px;
                
            }
            
            iframe {
                position:relative;
                width:100%;
                position:relative;
                padding-bottom:20px;
            }

        </style>
    </head>
    <body onload="getCurrentLocation();">
        <center>
        <table style="border: 2px solid #c2c2d6; width: 600px;background-color: #f9f9f9;">
        <div id="htmlcontent" style="display: none;">
        <tr style="border: 1px solid black;">
            <td style="font-size:30px;padding-left:0px;"><b><i><center> Product Search <center><div id="title"></div></i><b></td>
        </tr>
        <tr>
            <div id="form_elements">
            <form method="post" id="searchForm" action="" >
                    <tr>
                    <td id="form_align">
                        <label for="keyword">Keyword</label>
                        <input id="keyword_id" name="keyword" type="text" required value="<?php echo isset($_POST['keyword']) ? $_POST['keyword'] : '' ?>">
                    </td>
                    </tr>
                    
                    <tr>
                    <td  id="form_align">
                        <label for="category">Category</label>
                        <select name="category">
                            <option id="all_id" value="all" <?php echo isset($_POST['category']) ? ($_POST['category'] == 'all' ? ' selected' : '') : '' ?>>All Categories</option>
                            <option value="550" <?php echo isset($_POST['category']) ? ($_POST['category'] == '550' ? 'selected = "selected"' : '') : '' ?>>Art</option>
                            <option value="2984" <?php echo isset($_POST['category']) ? ($_POST['category'] == '2984' ? 'selected = "selected"' : '') : '' ?>>Baby</option>
                            <option value="267" <?php echo isset($_POST['category']) ? ($_POST['category'] == '267' ? 'selected = "selected"' : '' ) :''?>>Books</option>
                            <option value="11450" <?php echo isset($_POST['category']) ? ($_POST['category'] == '11450' ? 'selected = "selected"' : '') : '' ?>>Clothing, Shoes & Accessories</option>
                            <option value="58058" <?php echo isset($_POST['category']) ? ($_POST['category'] == '58058' ? 'selected = "selected"' : '') : '' ?> >Computers/Tablets & Networking</option>
                            <option value="26395" <?php echo isset($_POST['category']) ? ($_POST['category'] == '26395' ? 'selected = "selected"' : '') : '' ?>>Health & Beauty</option>
                            <option value="11233" <?php echo isset($_POST['category']) ? ($_POST['category'] == '11233' ? 'selected = "selected"' : '') : ''?>>Music </option>
                            <option value="1249" <?php echo isset($_POST['category']) ? ($_POST['category'] == '1249' ? 'selected = "selected"' : '') : '' ?>>Video Games & Consoles</option>
                        </select>
                    </td>
                    </tr>
                    
                    <tr>
                    <td  id="form_align">
                        <label for="condition">Condition</label>
                        <div id="condition"><input type="checkbox" id = "new_id" name="new" value="New" <?php echo isset($_POST['new']) ? 'checked' : '' ?>>New</div>
                        <div id="condition"><input type="checkbox" id = "used_id"  name="used" value="Used" <?php echo isset($_POST['used']) ? 'checked' : '' ?>>Used</div>
                        <div id="condition"><input type="checkbox" id = "unspecified_id"  name="unspecified" value="Unspecified" <?php echo isset($_POST['unspecified']) ? 'checked' : '' ?>>Unspecified</div>            
                    </td>
                    </tr>
                    
                    <tr>
                    <td id="form_align">
                        <label>Shipping options</label>
                        <div id ="shipping">
                            <input type="checkbox" id = "localpickup_id" name="localpickup" value="Local Pickup" <?php echo isset($_POST['localpickup']) ? 'checked' : '' ?>>Local Pickup
                        </div>
                        <div id ="shipping">
                            <input type="checkbox"id="freeshipping_id" name="freeshipping" value="Free Shipping" <?php echo isset($_POST['freeshipping']) ? 'checked' : '' ?>>Free Shipping
                        </div>
                    </td>
                    </tr>
                    
                    <tr>
                    <td id="form_align">
                        <label style="padding-right:30px">
                        <input type="checkbox" name="nearbyName" id="nearbyName" value="Enable Nearby Search" onclick="enableNearBy()" <?php echo isset($_POST['nearbyName']) ? 'checked ' : '' ?>>Enable Nearby Search
                        </label>
                        <div id="nearby">
                            <input type="text" id="miles_id" name="miles" disabled style="width: 50px;" placeholder="10" value="<?php echo isset($_POST['miles']) ? $_POST['miles'] : '' ?>">
                            <label>
                            miles from
                            </label>
                            <div id="mile">
                                    <input type="radio"  name= "dis" id="here" onclick="clearZipCode(); changeZipReq(); getCurrentLocation();" disabled value="here" <?php echo isset($_POST['dis']) ? ($_POST['dis'] != 'zip' ? 'checked' : '') : 'checked' ?>>Here <br>
                                    <input type="radio" name ="dis" id="zip" onclick="changeZipReq(); enablePointerEvent();" disabled value="zip" <?php echo isset($_POST['dis']) ? ($_POST['dis'] == 'zip' ? 'checked' : '') : '' ?>>
                                    <input type="text" id="zipcode" name = "zipcode" placeholder="zip code" disabled value="<?php echo isset($_POST['zipcode']) ? $_POST['zipcode'] : '' ?>">
                            </div>
                        </div>
                    </tr>
                    </td>
                    <tr>
                    <td style="text-align:center;padding-top:40px;padding-bottom:20px;">
                        <input type="submit" id="form_submit" name="submit" value="Search" disabled></input>
                        <input type="button" value="Clear" onclick = "resetAllFields()"></input>
                    </td>
                    <?php echo isset($_POST['nearbyName']) ? ' <script type="text/javascript">enableNearBy();</script>' : ''?>
                            <?php echo isset($_POST['dis']) ? ($_POST['dis'] != 'zip' ? ' <script type="text/javascript">getCurrentLocation();</script>' : '') : '' ?>
							<?php echo isset($_POST['dis']) ? ($_POST['dis'] == 'zip' ? ' <script type="text/javascript">enablePointerEvent();</script>' : '') : ''?>
                    <tr>
            </form>
            </div>
        </td>
        </tr>
        </table>
        <div id ="result">
        </div>
        <div id = "display_website">
            <div id = "display_text">click to show seller message</div>
            <div id ="display_image">
                <a href='#' onclick='displayHTML();return false;'><img src ='http://csci571.com/hw/hw6/images/arrow_down.png' height ='20px' width='40px'></a>
            </div>
        </div>
        <div id ="website">
        </div>
        <div id = "display_similar">
            <div id = "display_similar_text">click to show similar items</div>
            <div id ="display_similar_image">
                <a href='#'onclick='displaySimilar();return false;'><img src ='http://csci571.com/hw/hw6/images/arrow_down.png' height ='20px' width='40px'></a>
            </div>
        </div>
        <div id= "similar">
        </div>
        <div id = "end"></div>
        </center>
        <?php
            if(isset($_POST["submit"])) {
            
                $responseEncoding = 'JSON';   // Format of the response
                $endpoint = 'http://svcs.ebay.com/services/search/FindingService/v1';
                // Construct the FindItems call*/
                $apicall = $endpoint."?OPERATION-NAME=findItemsAdvanced"
                            . "&SERVICE-VERSION=1.0.0"
                            . "&paginationInput.entriesPerPage=20&SECURITY-APPNAME=MukundMu-Webtech-PRD-b16e081a6-7cf4af77" //replace with your app id
                            . "&RESPONSE-DATA-FORMAT=".$responseEncoding;
                            
                $apicall .= "&keywords=".urlencode($_POST['keyword']);
                if($_POST['category'] != "all") {
                    $apicall .= "&categoryId=".$_POST['category'];
                }
                $itemfil = -1;
                $itemfil = $itemfil + 1;
                $apicall .= "&itemFilter($itemfil).name=HideDuplicateItems";
                $apicall .= "&itemFilter($itemfil).value=true";
                if(isset($_POST['new']) == 1 || isset($_POST["used"])  == 1 || isset($_POST["unspecified"]) == 1) {
                    $itemfil = $itemfil + 1;
                    $i=0;
                    $apicall .= "&itemFilter($itemfil).name=Condition";
                    if(isset($_POST['new']) == 1) {
                        $apicall .= "&itemFilter($itemfil).value($i)=New";
                        $i = $i + 1;
                    }
                    if(isset($_POST["used"])  == 1) {
                        $apicall .= "&itemFilter($itemfil).value($i)=Used";
                        $i = $i + 1;
                    }
                    if(isset($_POST["unspecified"]) == 1) {
                        $apicall .= "&itemFilter($itemfil).value($i)=Unspecified";
                        $i = $i + 1;
                    }
                }
                if(isset($_POST["localpickup"])  == 1) {
                    $itemfil = $itemfil + 1;
                    $apicall .=    "&itemFilter($itemfil).name=LocalPickupOnly";
                    $apicall .= "&itemFilter($itemfil).value=true";
                }
                if(isset($_POST["freeshipping"])  == 1) {
                    $itemfil = $itemfil + 1;
                    $apicall .=    "&itemFilter($itemfil).name=FreeShippingOnly";
                    $apicall .= "&itemFilter($itemfil).value=true";
                }
                if(isset($_POST["nearbyName"])) {
                    $itemfil = $itemfil + 1;
                    $apicall .= "&itemFilter($itemfil).name=MaxDistance";
					$miles = "10";
					if(isset($_POST['miles']) && $_POST['miles'] != '') {
						$miles = $_POST['miles'];
					}
                    $apicall .= "&itemFilter($itemfil).value=".$miles;
                    if(isset($_POST['dis'])) {
                        if($_POST['dis'] == 'zip') {
                            $apicall .= "&buyerPostalCode=".$_POST['zipcode'];
                        } else {
                            $zipcode =  $_POST['dis'];//get the zip code for current
                            $apicall .= "&buyerPostalCode=".$zipcode;
                        }
                    }
                }
                $results = '';
                ini_set("allow_url_fopen", 1);
                //echo $apicall;
                // Load the call and capture the document returned by the Finding API
                $resp = file_get_contents($apicall);
                $response = json_decode($resp);
                $i = 0;
                $stack = array();
                $res = $response->findItemsAdvancedResponse[0]->ack[0];
                if($res == 'Success') {
                    if (array_key_exists('searchResult', $response->findItemsAdvancedResponse[0])) {
                            if (array_key_exists('item', $response->findItemsAdvancedResponse[0]->searchResult[0])) {
                                $items = $response->findItemsAdvancedResponse[0]->searchResult[0]->item;
                                    $i = 0;
                                    $stack = array();
                                    foreach($items as $item) {
                                        $i = $i + 1;
                                        $index = $i;
                                        $id=$item->itemId;
                                        if (array_key_exists('galleryURL', $item) &&  count($item->galleryURL) > 0) {
                                          $photo = $item->galleryURL[0];
                                        } else {
                                          $photo = "N/A";
                                        }
                                        $name = $item->title[0];
                                        $name1 = str_replace('"', '', $name);
                                        if (array_key_exists('sellingStatus', $item)) {
                                                $price = sprintf("%01.2f", $item->sellingStatus[0]->currentPrice[0]->__value__);
                                        } else {
                                            $price = "N/A";
                                        }
                                        if(array_key_exists('postalCode', $item) &&  count($item->postalCode) > 0) {
                                            $zipcode = $item->postalCode[0];   
                                        } else {
                                            $zipcode = "N/A";   
                                        }
                                       
                                        if (array_key_exists('condition', $item) &&  count($item->condition) > 0) {
                                            if (array_key_exists('conditionDisplayName', $item->condition[0])) {
                                                $cond = "";
                                                foreach($item->condition[0]->conditionDisplayName as $conditions) {
                                                    $cond = $cond.(string) $conditions;
                                                }
                                            }
                                        } else {
                                                $cond = 'N/A';
                                        }
                                        $ship = -1;
                                        if(array_key_exists('shippingInfo', $item) &&  count($item->shippingInfo) > 0) {
                                            if (array_key_exists('shippingServiceCost', $item->shippingInfo[0])) {
                                                if (array_key_exists('__value__', $item->shippingInfo[0]->shippingServiceCost[0])) {
                                                    $ship  = sprintf("%01.2f", $item->shippingInfo[0]->shippingServiceCost[0]->__value__);
                                                }
                                            }
                                        }

                                        if ($ship == -1) {
                                            $ship_option = 'N/A';
                                        } else if ($ship == 0.00) {
                                            $ship_option = 'Free Shipping';
                                        } else {
                                            $ship_option = (string) $ship;
                                            $ship_option = "$".$ship_option ;
                                        }

                                        $arr = array('Index' => $index, 'PHOTO' => $photo, 'NAME' => $name1, 'PRICE' => "$".$price,
                                        'ZIPCODE' => $zipcode,'CONDITION' => $cond,'SHIPPING_INFO' => $ship_option, "ID" => $id);

                                        array_push($stack, $arr);
                                    }
                                    $arr_post =  array('POST_DATA' => $_POST);
                                    array_push($stack, $arr_post);
                                    $jsonObj = json_encode($stack);
                                    echo "<script type='text/javascript'>parseJSONResults(".$jsonObj.");</script>";     
                                } else {
                                    $errorMessage = "No Records has been found";
                                    echo "<script type='text/javascript'>displayErrorMessage('".$errorMessage."');</script>";
                                }
                        }
                } else if($res == 'Failure'){
                    $errorMessage = $response->findItemsAdvancedResponse[0]->errorMessage[0]->error[0]->message[0];
                    echo "<script type='text/javascript'>displayErrorMessage('".$errorMessage."');</script>";     
                }
                }
            
            
            function getDetails($id) {
                        $responseEncoding = 'JSON';   // Format of the response
                        $endpoint = 'http://open.api.ebay.com/shopping';
                        // Construct the FindItems call*/
                        $apicall = $endpoint."?callname=GetSingleItem"
                                    . "&appid=MukundMu-Webtech-PRD-b16e081a6-7cf4af77" //replace with your app id
                                    . "&siteid=0"
                                    . "&version=967"
                                    . "&responseencoding=".$responseEncoding;
                                    
                        $apicall .= "&ItemID=".$id;
                        $apicall .= "&IncludeSelector=Description,Details,ItemSpecifics";
                        $results = '';
						//echo $apicall;
                        ini_set("allow_url_fopen", 1);
                        // Load the call and capture the document returned by the Shopping API
                        $resp = file_get_contents($apicall);
                        $response = json_decode($resp);
                        $i = 0;
                        $status = $response->Ack;
                        if($status == "Success") {
                            if (array_key_exists('Item', $response)) {
                                $item = $response->Item;
                                if (array_key_exists('Description', $item)) {
                                    $description = $item->Description;
                                } else {
                                    $description = "N/A";
                                }
                                if (array_key_exists('PictureURL', $item) && count($item->PictureURL) > 0) {
                                  $photo = $item->PictureURL[0];
                                } else {
                                  $photo = "N/A";
                                }
                                if (array_key_exists('Title', $item)) {
                                    $title = $item->Title;
                                    $title = str_replace('"', '', $title);
                                } else {
                                    $title = "N/A";
                                }
                                if (array_key_exists('Subtitle', $item)) {
                                    $Subtitle = $item->Subtitle;
                                    $Subtitle = str_replace('"', '', $Subtitle);
                                } else {
                                    $Subtitle = "N/A";
                                }
                                if (array_key_exists('CurrentPrice', $item)) {
                                    if (array_key_exists('Value', $item->CurrentPrice)) {
                                        $price = sprintf("%01.2f", $item->CurrentPrice->Value);
										$price .=' ' ;
                                        $price .= $item->CurrentPrice->CurrencyID;
                                    }
                                } else {
                                    $price = "N/A";
                                }
                                
                                if(array_key_exists('Location', $item)) {
                                    $location = $item->Location;
                                } else {
                                    $location = "N/A";   
                                }
                                    
                                if (array_key_exists('PostalCode', $item)) {
                                    if ($location != "N/A") {
										$location .= ', ';
                                        $location .= $item->PostalCode;
                                    } else {
									   $location .= $item->PostalCode;
									}
                                }
                                    
                                if(array_key_exists('Seller', $item)) {
                                    if(array_key_exists('UserID', $item->Seller)) {
                                        $seller = $item->Seller->UserID;
                                    }  
                                } else {
                                    $seller = "N/A";   
                                }

                                if(array_key_exists('ReturnPolicy', $item)) {
                                    if (array_key_exists('ReturnsAccepted', $item->ReturnPolicy)) {
                                        $returnpolicy = $item->ReturnPolicy->ReturnsAccepted;
                                    } else {
                                        $returnpolicy = "N/A";
                                    }
                                    if (array_key_exists('ReturnsWithin', $item->ReturnPolicy) && $returnpolicy != "N/A") {
                                        $returnpolicy .= " within ".$item->ReturnPolicy->ReturnsWithin;
                                    }
                                } else {
                                    $returnpolicy = "N/A";
                                }
                                    
                                    
                                $arr = array('Photo' => $photo, 'Title' => $title, 'Subtitile' => $Subtitle, 'Price' => $price,
                                'Location' => $location,'Seller' => $seller,'Return Policy (US)' => $returnpolicy);
                                $arr['description'] = ($description);
                                if(array_key_exists('ItemSpecifics', $item)) {
                                    if(array_key_exists('NameValueList', $item->ItemSpecifics)) {
                                        foreach($item->ItemSpecifics->NameValueList as $nvPair) {
                                            $name = (string) $nvPair->Name;
                                            $value = (string)  $nvPair->Value[0];
                                            $arr[$name] = $value;
                                        }
                                    }
                                } else {
									$arr['No Detail Info from seller'] = "N/A";
								}
                                $arr_similar = getSimilar($id);
                                $arr['similar'] = $arr_similar;
                                $arr['POST_DATA'] = $_POST;
                                $jsonObj = json_encode($arr);
                                #echo $jsonObj;
                                echo "<script type='text/javascript'>parseJSONDetails(".$jsonObj.");</script>";     
                            } else {
                                 $errorMessage = "Item Details not found";
                                 echo "<script type='text/javascript'>displayErrorMessage('".$errorMessage."');</script>";
                            }
                    } else {
                        $errorMessage = $response->Errors[0]->ShortMessage;
                        echo "<script type='text/javascript'>displayErrorMessage('".$errorMessage."');</script>";
                    }
            }

            function getSimilar($id) {
                $responseEncoding = 'JSON';   // Format of the response
                $endpoint = 'https://svcs.ebay.com/MerchandisingService';
                // Construct the FindItems call*/
                $apicall = $endpoint."?OPERATION-NAME=getSimilarItems"
                            . "&SERVICE-NAME=MerchandisingService"
                            . "&SERVICEVERSION=1.1.0"
                            . "&CONSUMER-ID=MukundMu-Webtech-PRD-b16e081a6-7cf4af77" //replace with your app id
                            . "&RESPONSE-DATA-FORMAT=".$responseEncoding
                            . "&REST-PAYLOAD"
                            . "&maxResults=8";
                   
                $apicall .= "&itemId=".$id;
                $results = '';
				//echo $apicall;
                ini_set("allow_url_fopen", 1);
                // Load the call and capture the document returned by the Shopping API
				//echo $apicall;
                $resp = file_get_contents($apicall);
                $response = json_decode($resp);
                $stack = array();
                if (array_key_exists('itemRecommendations', $response->getSimilarItemsResponse)) {
                    if (array_key_exists('item', $response->getSimilarItemsResponse->itemRecommendations)) {
                        $items = $response->getSimilarItemsResponse->itemRecommendations->item;
						if(sizeof($items) > 0) {
							foreach($items as $item) {
								if (array_key_exists('itemId', $item)) {
									$itemId = $item->itemId;
								}
								if (array_key_exists('title', $item)) {
									$title = $item->title;
									$title = str_replace('"', '', $title);
								}
								if (array_key_exists('imageURL', $item)) {
								  $photo = $item->imageURL;
								} else {
								  $photo = "http://pics.ebaystatic.com/aw/pics/express/icons/iconPlaceholder_96x96.gif";
								}
								if (array_key_exists('buyItNowPrice', $item)) {
									$price = sprintf("%01.2f", $item->buyItNowPrice->__value__);
									//$curr = $item->buyItNowPrice->@currencyId;
									$cost = (string) $price;//. (string) $curr;
								}
								$arr = array('ITEMID' => $itemId, 'PHOTO' => $photo, 'NAME' => $title, 'PRICE' => $cost);
								array_push($stack, $arr);
							}
							return $stack;
						} else {
							return "N/A";
						}
                    } else {
						return "N/A";
					}
                }
            }
            
            if (isset($_POST['details'])) {
                    getDetails($_POST['details']);
            }

        ?>
</body>
</html>