<#assign feed=doc/>
<html>
	<head>
		<title>${feed.entry[0].title[0]?html}</title>
		
		<!-- mobile viewport -->
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"> 
				
		<!-- jquery -->
		<link rel="stylesheet" href="./jquery/mobile/jquery.mobile-1.0rc2.min.css" />
		<script src="./jquery/jquery-1.7.min.js"></script>
		<script src="./jquery/mobile/jquery.mobile-1.0rc2.min.js"></script>
		
        <style>
	        @media all and (min-width: 650px){
	                .content-secondary {
	                        text-align: left;
	                        float: left;
	                        width: 45%;
	                        background: none;
	                }
	
	                .content-primary {
	                        width: 45%;
	                        float: right;
	                        margin-top: 30px;
	                        margin-right: 1%;
	                        padding-right: 1%;
	                }
	        }
        </style>
		
		
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
					<img style='max-width: 100%; padding-bottom: 10px;' id="cover-image" src="${cover[0].@href?html}"/>
					</#if>
				
				</div><!--/content-primary-->

                <div class="content-primary">
					
					<#assign files=feed.entry[0]["link[contains(@rel,'acquisition')]"]/>
					<#assign related=feed.entry[0]["link[contains(@rel,'related')]"]/>
		
					<ul data-role="listview">	
					<#if files?size &gt; 0>		
					<!-- files -->
					<li data-role="list-divider">Downloads</li>		
					<#list files as file>
						<li><a href="${file.@href}" rel="external"><p>${file.@title}</p></a></li>
					</#list>
					</#if>
					
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
			
			<div data-role="footer"> 
			</div>
		</div>
	</body>
</html>