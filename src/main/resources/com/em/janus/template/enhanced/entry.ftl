<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<#assign feed=doc/>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<!-- encoding -->
		<meta charset="utf-8">

		<!-- basic viewport info -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		
		<!-- style -->
		<style type="text/css">
			body {
			    padding-top: 60px;
				padding-bottom: 40px;
			}
			.sidebar-nav {
				padding: 9px 0;
			}
		</style>
		<link href="style/enhanced/bootstrap.min.css" rel="stylesheet">
		<link href="style/enhanced/janus.css" rel="stylesheet">	
			
		<title>Janus: ${feed.entry[0].title[0]?html}</title>
	</head>
	<body>
	
		<!--navigation -->
	    <div class="navbar navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </a>
	          <a class="brand" href="./index.html">Janus</a>
	          <div class="nav-collapse">
	            <ul class="nav pull-right">
	              <li class=""><a href="./author_index.html">Authors</a></li>
	              <li class=""><a href="./book_index.html">Books</a></li>
	              <li class=""><a href="./series_index.html">Series</a></li>
	              <li class=""><a href="./tag_index.html">Tags</a></li>
	            </ul>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>	
	
	
	    <!-- content -->
	    <div class="container">
			
			<!-- top row, basic information, details, and right-pushed thumb -->
			<div class='row'>
				<!-- information -->
				<div class="span12">
					<div class="well">
						<div class="row-fluid">
						
							<#assign cover=feed.entry[0]["link[contains(@rel,'cover')]"]/>
							
							<div class="span9">
								<!-- phone visible thumb -->
								<img align="right" class="thumbnail visible-phone" src="${cover[0].@href?html}&w=65&h=108"/>

								<!-- title or title with subtitle -->
								<#if feed.entry[0].title[0]?contains(":")>
								<#assign titleHeader>${feed.entry[0].title[0]?substring(0,feed.entry[0].title[0]?index_of(":"))?html}</#assign>
								<#assign subtitleHeader>${feed.entry[0].title[0]?substring(feed.entry[0].title[0]?index_of(":")+1)?html}</#assign>
									<h2>${titleHeader}</h2><h6>${subtitleHeader}</h6>
								<#else>
									<h2>${feed.entry[0].title[0]?html}</h2>
								</#if>

								<!-- authors -->
								<div class="row-fluid entryRow">
									<div class="span9">
									<#assign authors=feed.entry[0].author>
									<h5>author<#if authors?size &gt; 1>s</#if>:</h5> <#list authors as author><a href="./${author.uri?replace(".xml",".html")?html}">${author.name}</a><#if author_has_next>,&nbsp;</#if></#list>
									</div>
								</div>
								
								<!-- downloads -->
									<#assign files=feed.entry[0]["link[contains(@rel,'acquisition')]"]/>
									<#if files?size &gt; 0>
								<div class='row-fluid entryRow'>	
									<!-- download links -->
									<div class="span4">
										<h5>download ebook:</h5>
										<#list files as file><p><a href="${file.@href}"><i class="icon-download"></i>&nbsp;${file.@title}</a></p></#list>
									</div>		
								</div>						
									</#if>
									
								<!-- book email feature -->
									<#if files?size &gt; 0>
								<div class='row-fluid'>
									<!-- tags -->
									<div id="formSpan" class="span9">
										<h5>email ebook:</h5>
										<form id="emailForm" class="form-inline">
											<input type="hidden" name="id" id="bookId" value="${feed.raw[0]?html}"/>
											<div class="control-group">
					        	 				<div class="controls">
							        	 			<div class="input-prepend input-append" style="width: 100%">
							        	 				<#assign ext=""/>
							        	 				<#if files?size == 1>
														<input type="hidden" name="ext" id="bookExt" value="${files[0].@href?substring(files[0].@href?last_index_of(".")+1)?html}"/>
														<#assign ext><span class="add-on">${files[0].@href?substring(files[0].@href?last_index_of(".")+1)?html}</span></#assign>
														<#elseif files?size &gt; 1>
														<#assign ext><select class="span2" id="bookExt" name="ext"><#list files as file><option>${file.@href?substring(files[0].@href?last_index_of(".")+1)?html}</option></#list></select></#assign>
														</#if>
														${ext}<input id="emailTo" type="text" placeholder="sample@kindle.com"/><button class="btn" type="button" id="emailSubmit">&nbsp;<i class="icon-envelope"></i>&nbsp;</button>
													</div>
												</div>
											</div>
										</form>
									</div>
								</div>
									</#if>
								
									<#assign tags=feed.entry[0].tag>
									<#if tags?size &gt; 0>
								<!-- tags -->									
								<div class="row-fluid">
									<div class="span9">									
									<h5>tags:</h5><#list tags as tag><a href="./tag.html?id=${tag.id?html}&mode=enhanced">${tag.name?html}</a><#if tag_has_next>,&nbsp;</#if></#list>
									</div>
								</div>
									</#if>									
														
									<#assign content=feed.entry[0].content[0]!""/>
									<#if content != "">
								<!-- comment -->
								<div class="row-fluid entryRow">
									<div class="span9">${content}</div>
								</div>
									</#if>
																	
							</div> <!-- end span9 -->
							<div class="span3 bookEntryImageContainer hidden-phone">
								<!-- thumbs -->
								<img class="pull-right thumbnail" src="${cover[0].@href?html}&w=187&h=290"/>
							</div>
						</div> <!-- end row inside of well -->
										
					</div> <!-- end well row -->
				</div> <!-- end main span -->
			</div>	<!-- end content row -->		
			
		</div> <!-- end content -->	

		<!-- bootstrap standard says place stuff at the end of the body -->
		<script src="jquery/jquery-1.7.2.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/cookies.js"></script>
		
		<!-- page function -->
		<script>
			var emailCookieKey = "kindle_email";
		
			//when the document is ready
			$(document).ready(function(){
				var emailCookie = getCookie(emailCookieKey);
				if(!emailCookie || emailCookie == "" ||  emailCookie == emailCookieKey) {
					emailCookie = "@kindle.com";
				} else {
					$('#emailTo').val(emailCookie);
				}
			});
			
			//email submit function
			var emailSubmitFunction = function() {
				//disable the email form
				$('#emailTo').prop('disabled', true);
				
				//get email so that it can be set in cookie and in hidden form
				var toEmail = $('#emailTo').val();
				
				var mime = $('#bookMime').val();

				//get form data and make string				
				var dataString = $('#emailForm').find("input").serialize() + "&to=" + encodeURI(toEmail);
							
				//use ajax to submit form
				$.ajax({  
					type: "POST",
					url: "email?ajax=true",
					data: dataString,
					cache: false		
				})
				.done(function() {
					//show status
					createAlert('Yay!', 'Your email was successfully sent', 'alert-success');
				})
				.fail(function() {
					//show status
					createAlert('Oops', 'Your email was not sent.', 'alert-error');

				})
				.always(function() {
					//always re-enable text box
					$('#emailTo').prop('disabled', false);
				});
				;
			
				setCookie(emailCookieKey, toEmail);
			};
			
			//now to create a way to submit the form over ajax, wait for a response, and set the cookie
			$('#emailSubmit').click(emailSubmitFunction);
			$('#emailSubmitPhone').click(emailSubmitFunction);		
			
			function createAlert(header, message, type) {
				var alertHTML = "";
				alertHTML += '<div class="span6 alert alert-block ' + type +'">';
				alertHTML += '<a class="close" data-dismiss="alert" href="#">×</a>';
				alertHTML += '<h4 class="alert-heading">' + header + '</h4> ' + message;									   
				alertHTML += '</div>';
			
				//append to form span so that alert is shown
				$('#formSpan').append(alertHTML);
			}
		</script>
	</body>
</html>