<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Index for ${series.name?xml}</title>
	
	<id>janus:series:${series.id?c}</id>
	
	<content type="text">index of series ${series.name?xml}</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#if authors?size &gt; 1>
	<entry>
		<title>Authors of this series</title>
		<id>janus:authors</id>

		<content type="text">
			Link to the ${authors?size} authors who contributed to ${series.name?xml}		
		</content>
		
		<link href="./authors.xml?series=${series.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	<#elseif authors?size == 1>
	<entry>
		<title>${authors?first.name?xml}</title>
		<id>janus:authors</id>

		<content type="text">
			Link to the author of this series.		
		</content>
		
		<link href="./author.xml?id=${authors?first.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	</#if>

	<#list books as book>
	<entry>
		<title>${book.seriesIndex?c} - ${book.title?xml}</title>
		<id>janus:series:${series.id?c}:book:${book.id?c}</id>
		<#list book.authors as author>
		<author>
			<name>${author.name?xml}</name>
			<uri>./author.xml?id=${author.id?c}&amp;mode=${mode}</uri>
		</author>
		</#list>
		
		<content type="text">
			${series.name} book number ${book.seriesIndex?c}  
		</content>
		
	    <#list book.fileInfo as fi>
	    <link href="./file?id=${book.id?c}&amp;ext=.${fi.extension?url}" type="${fi.mimeType?xml}" rel="http://opds-spec.org/acquisition" title="Download this ebook as ${fi.extension?upper_case?xml}" />
		</#list>    		
		<link href="./book.xml?id=${book.id?c}&amp;mode=${mode}" type="application/atom+xml;type=entry;profile=opds-catalog" rel="alternate"/>
		<#if mode != "aldiko">
		<link href="./book.xml?id=${book.id?c}&amp;mode=${mode}" type="application/atom+xml;type=entry;profile=opds-catalog"/>
	    <link href="./image?id=${book.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
		</#if>		
	    <link href="./image?id=${book.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />      
	</entry>
	</#list>

</feed>