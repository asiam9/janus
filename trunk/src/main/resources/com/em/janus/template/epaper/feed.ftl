<#assign feed=doc/>
<html>
	<head>
		<title>${feed.title[0]?html}</title>
		
		<!-- epaper grid style style -->
		<link rel="stylesheet" media="screen" type="text/css" href="./style/epaper/fluid_grid.css" />
		<link rel="stylesheet" media="screen" type="text/css" href="./style/epaper/epaper.css" />
	</head>
	<body>
	<!-- open container -->
	<div class="container container_10">
	
	<!-- title -->
	<div class='grid_8'>
		<h1>${feed.title[0]?html}</h1>
	</div>
	
	<!-- home link -->
	<div class='grid_10 prefix_9'>
		<p><a href="./index.html">Home</a></p>
	</div>
	
	<#assign next=feed["link[contains(@rel,'next')]"]/>
	
	<#list feed.entry as entry>
		<#assign link=entry["link[contains(@type,'type=feed')]"]/>
		<#assign book=entry["link[contains(@type,'type=entry')]"]/>
		<#assign thumb=entry["link[contains(@rel,'thumbnail')]"]/>
		
		<!-- feed item -->
		<div id='${entry.id?html}' class='grid_10'>
			<p>
			<#if thumb?size &gt; 0 && thumb[0].@href?length &gt; 1>
			<div class='grid_10'>
			<img src="${thumb[0].@href?html}"/>
			</div>
			</#if>
			<#if link?size &gt; 0>
			<div class='grid_10'><a href="${link[0].@href?replace(".xml",".html")?html}"><b>${entry.title[0]?html}</b></a></div>
			<#elseif book?size &gt; 0>
			<div class='grid_10'><a href="${book[0].@href?replace(".xml",".html")?html}&amp;type=book"><b>${entry.title[0]?html}</b></a></div>
			<#else>
			</#if>
			<div class='grid_10'>${(entry.content[0]!"")?html}</div>
			</p>
		</div>
	</#list>
	
	<!-- close container -->
	</div>

	</body>
</html>