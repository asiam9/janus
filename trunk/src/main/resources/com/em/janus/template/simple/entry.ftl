<#assign feed=doc/>
<html>
	<head>
		<title>${feed.entry[0].title[0]?html}</title>
	</head>
	<body>
	<table width="100%"><tr><td width="100%"><h1>${feed.entry[0].title[0]?html}</h1></td><td width="25%" align="right"><a href="./index.html">Home</a></td></tr></table>
	<table width="100%" border="3px" rules="none" frame="box">
		<tr>
			<td width="100%">
				<a href="javascript:window.history.back();" onClick="window.history.back();">Previous Page</a>
			</td>
		</tr>
	</table><br/>
	
	<table width="100%">
		<tr>
			<td width="50%" rowspan="2">
			<!-- cover -->
			<#assign cover=feed.entry[0]["link[contains(@rel,'cover')]"]/>
			<#if cover?size &gt; 0 && cover[0].@href?length &gt; 1>
			<img src="${cover[0].@href?html}&w=377&h=500"/>
			</#if>
			</td>
			<td width="50%">
			<!-- authors -->
			Written by:
			<ul>
			<#list feed.entry[0].author as author>
				<li><a href="./${author.uri?replace(".xml",".html")?html}">${author.name}</a></li>
			</#list>
			</ul>
			</td>
		</tr>
		<tr>
			<td>
			<!-- details -->
			${feed.entry[0].content[0].@@markup!""}
			</td>
		</tr>
		<tr>
			<td>
			<!-- aquisition links -->
			<#assign files=feed.entry[0]["link[contains(@rel,'acquisition')]"]/>
			<#list files as file>
			<a href="${file.@href}">${file.@title}</a><br>
			</#list>
			</td>
			<td>
			<!-- related links -->
			<#assign related=feed.entry[0]["link[contains(@rel,'related')]"]/>
			<#list related as rel>
			<a href="${rel.@href?replace(".xml",".html")?html}">${rel.@title}</a><br>
			</#list>
			</td>
		</tr>	
	</table>
	<br/>
	<table width="100%" border="3px" rules="none" frame="box">
		<tr>
			<td width="100%">
				<a href="javascript:window.history.back();" onClick="window.history.back();">Previous Page</a>
			</td>
		</tr>
	</table>	
	</body>
</html>