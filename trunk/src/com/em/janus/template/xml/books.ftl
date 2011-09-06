<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Books Index</title>
	
	<id>janus:books</id>
	
	<content type="text"><#if sort=="title">Alphabetical</#if> index of all books</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#list books as book>
	<entry>
		<title>${book.title?xml}</title>
		<id>janus:book:${book.id?c}</id>
		<#list book.authors as author>
		<author>
			<name>${author.name?xml}</name>
			<uri>./author.xml?id=${author.id?c}&amp;mode=${mode}</uri>
		</author>
		</#list>
		
		<published>${book.pubdate?date}</published>
		<timestamp>${book.timestamp?date}</timestamp>
		<modified>${book.lastModified?date}</modified>
		
		<content type="text">
			Written by<#list book.authors as author> ${author.name?xml}</#list><#if book.series?size &gt; 0>, &quot;<#list book.series as series>${series.name?xml}</#list>&quot; book number ${book.seriesIndex?string.number}</#if>		
		</content>
		
		<link href="./book.xml?id=${book.id?c}&amp;mode=${mode}" type="application/atom+xml;type=entry;profile=opds-catalog" rel="alternate"/>
		<#if mode != "aldiko">
		<link href="./book.xml?id=${book.id?c}&amp;mode=${mode}" type="application/atom+xml;type=entry;profile=opds-catalog"/>
	    <link href="./image?id=${book.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
		</#if>		
	    <link href="./image?id=${book.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />      
	</entry>
	</#list>
	
	<#if mode!="json">
	<link href="./books.xml?sort=${sort}&amp;index=${index?c}&amp;size=${size?c}&amp;mode=${mode}&amp;starts=${starts!""}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="next" title="next page"/>
	</#if>
	
</feed>