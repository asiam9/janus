<#assign feed=doc/>
<html>
	<head>
		<title>${feed.title[0]?html}</title>
		
		<!-- mobile viewport -->
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, maximum-scale=1"> 

		<!-- jquery style -->
		<link rel="stylesheet" href="./jquery/mobile/jquery.mobile-1.1.1.min.css" />
					
		<!-- jquery -->
		<script type="text/javascript" src="./jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="./jquery/mobile/jquery.mobile-1.1.1.min.js"></script>
		
		<!-- cookie management -->
		<script type="text/javascript" src="./js/cookies.js"></script>					
		
		<!-- janus style -->
		<link rel="stylesheet" href="./style/mobile/mobile.css" />
	</head>
	<body>
		<div data-role="page" id="content-page" data-add-back-btn="true" data-theme="b">
			<!-- jquery mobile header bar -->
			<div data-role="header"> 
				<h1>${feed.title[0]?html}</h1> 
				<a href="./index.html?mode=mobile" data-icon="home" data-iconpos="notext" data-direction="reverse" class="ui-btn-right jqm-home">Home</a>
			</div> 
			
			<!-- content -->
			<div data-role="content" data-add-back-btn="true">
				<div class="content-only">	
					<ul data-role="listview">	
						<#list feed.entry as entry>
							<#assign link=entry["link[contains(@type,'type=feed')]"]/>
							<#assign book=entry["link[contains(@type,'type=entry')]"]/>
							<#assign thumb=entry["link[contains(@rel,'thumbnail')]"]/>
			
							<li>
								<#if link?size &gt; 0>
									<a href="${link[0].@href?replace(".xml",".html")?html}">
								<#elseif book?size &gt; 0>
									<a href="${book[0].@href?replace(".xml",".html")?html}&amp;type=book">
								<#else>
								</#if>
								<#if thumb?size &gt; 0 && thumb[0].@href?length &gt; 1>
									<img src="${thumb[0].@href?html}"/>
								</#if>
								<#if link?size &gt; 0>
									<p><b>${entry.title[0]?html}</b></p>
								<#elseif book?size &gt; 0>
									<p><b>${entry.title[0]?html}</b></p>
								<#else>
								</#if>
								<p>${(entry.content[0]!"")?html}</p>
								</a>
							</li>
						</#list>
						
						<#assign next=feed["link[contains(@rel,'next')]"]/>
						<#if next?size &gt; 0 && next[0].@href?length &gt; 1>
						<li><a href="${next[0].@href?replace(".xml",".html")?html}" data-transition="slideup" data-prefetch><center>Next Page</center></a></li>
						</#if>
						
					</ul>
				</div>
			</div>
			
			<div class="janus-footer" data-role="footer"> 
			</div>
		</div>
	
	</body>
</html>