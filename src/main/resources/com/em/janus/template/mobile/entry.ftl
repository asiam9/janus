<#assign feed=doc/>
<html>
	<head>
		<title>${feed.entry[0].title[0]?html}</title>
		
		<!-- mobile viewport -->
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, maximum-scale=1"> 
		
		<!-- jquery style -->
		<link rel="stylesheet" href="./jquery/mobile/jquery.mobile-1.0.min.css" />
					
		<!-- jquery -->
		<script type="text/javascript" src="./jquery/jquery-1.6.4.min.js"></script>
		<script type="text/javascript" src="./jquery/mobile/jquery.mobile-1.0.min.js"></script>
		
		<!-- cookie management -->
		<script type="text/javascript" src="./js/cookies.js"></script>					

		<!-- janus style -->
		<link rel="stylesheet" href="./style/mobile/mobile.css" />
	</head>
	<body>
		<div data-role="page" id="content-page" data-add-back-btn="true" data-theme="b">
			<!-- jquery mobile header bar -->
			<div data-role="header"> 
				<h1>${feed.entry[0].title[0]?html}</h1> 
				<a href="./index.html?mode=mobile" data-icon="home" data-iconpos="notext" data-direction="reverse" class="ui-btn-right jqm-home">Home</a>
			</div> 
			
			<div data-role="content" data-add-back-btn="true">
				<div class="content-secondary">	
			
					<!-- cover -->
					<#assign cover=feed.entry[0]["link[contains(@rel,'cover')]"]/>
					<#if cover?size &gt; 0 && cover[0].@href?length &gt; 1>
					<img class="cover-image" src="${cover[0].@href?html}"/>
					</#if>
				
				</div><!--/content-primary-->

                <div class="content-primary">
					
					<#assign files=feed.entry[0]["link[contains(@rel,'acquisition')]"]/>
					<#assign related=feed.entry[0]["link[contains(@rel,'related')]"]/>
		
					<ul data-role="listview" data-inset="true">	
					<#if files?size &gt; 0>		
					<!-- files -->
					<li data-role="list-divider">Downloads</li>		
					<#list files as file>
						<li><a href="${file.@href}" rel="external"><p>${file.@title}</p></a></li>
					</#list>
					</#if>
					
					<li data-role="list-divider">Email to Kindle</li>
					<li>
					<form action="./email" method="post" id="emailForm">
						<fieldset data-role="controlgroup" data-type="horizontal" data-role="fieldcontain">
		         			<input type="text" name="to" id="to"/>
		         			<input type="hidden" name="id" id="id" value="${feed.raw[0]?html}"/>
						</fieldset>
						<button type="submit">email</button>
					</form>
					</li>
					
					<#if feed.entry[0].author?size &gt; 0> 
					<!-- authors -->
					<li data-role="list-divider">Author(s) of this book</li>
					<#list feed.entry[0].author as author>
						<li><a href="./${author.uri?replace(".xml",".html")?html}"><p>${author.name}</p></a></li>
					</#list>
					</#if>
					
					<#if related?size &gt; 0>
					<!-- related links -->
					<li data-role="list-divider">Related Links</li>
					<#list related as rel>
						<li><a href="${rel.@href?replace(".xml",".html")?html}"><p>${rel.@title}</p></a></li>
					</#list>
					</#if>
					
					</ul>	
				</div>
			</div>		
			
			<div class="janus-footer" data-role="footer"> 
			</div>
			
			<!-- page specific script -->
			<script>		
				$('#content-page').live('pageinit',function(event){
					var emailCookie = getCookie("kindle_email");
					if(!emailCookie || emailCookie == "") {
						emailCookie = "sample@kindle.com";
					}
					$('#to').val(emailCookie);
				});	
				
				$('#emailForm').submit(function() {
					var emailCookie = $('#to').val();
					setCookie("kindle_email",emailCookie);
					return true;
				});
			</script>
		</div>
	</body>
</html>