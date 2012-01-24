<#assign feed=doc/>
<html>
	<head>
		<title>${feed.title[0]?html}</title>
	</head>
	<body>
	<table width="100%"><tr><td width="100%"><h1>${feed.title[0]?html}</h1></td><td width="25%" align="right"><a href="./index.html">Home</a></td></tr></table>
	<#assign next=feed["link[contains(@rel,'next')]"]/>
	<table width="100%" border="3px" rules="none" frame="box">
		<tr>
			<td width="50%">
				<a href="javascript:window.history.back();" onClick="window.history.back();">Previous Page</a>
			</td>
			<td width="50%" align="right">
				<#if next?size &gt; 0 && next[0].@href?length &gt; 1>
				<a href="${next[0].@href?replace(".xml",".html")?html}">Next Page</a>
				</#if>
			</td>
		</tr>
	</table><br/>			
	<#list feed.entry as entry>
		<#assign link=entry["link[contains(@type,'type=feed')]"]/>
		<#assign book=entry["link[contains(@type,'type=entry')]"]/>
		<#assign thumb=entry["link[contains(@rel,'thumbnail')]"]/>
		<table 'id=${entry.id?html}'>
			<tr>
				<#if thumb?size &gt; 0 && thumb[0].@href?length &gt; 1>
				<td rowspan="2">
				<img src="${thumb[0].@href?html}"/>
				</td>
				</#if>
				<#if link?size &gt; 0>
				<td><a href="${link[0].@href?replace(".xml",".html")?html}"><b>${entry.title[0]?html}</b></a></td>
				<#elseif book?size &gt; 0>
				<td><a href="${book[0].@href?replace(".xml",".html")?html}&amp;type=book"><b>${entry.title[0]?html}</b></a></td>
				<#else>
				</#if>
			</tr>
			<tr>
				<td>${(entry.content[0]!"")?html}</td>
			</tr>
		</table>
	</#list>
	<br/>
	<table width="100%" border="3px" rules="none" frame="box">
		<tr>
			<td width="50%">
				<a href="javascript:window.history.back();" onClick="window.history.back();">Previous Page</a>
			</td>
			<td width="50%" align="right">
				<#if next?size &gt; 0 && next[0].@href?length &gt; 1>
				<a href="${next[0].@href?replace(".xml",".html")?html}">Next Page</a>
				</#if>
			</td>
		</tr>
	</table>	
	</body>
</html>