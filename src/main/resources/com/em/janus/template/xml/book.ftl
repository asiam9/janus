<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	
	<title>${book.title?xml}</title>
	<id>janus:book:${book.id?c}</id>
	<raw>${book.id?c}</raw>

 	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus Catalog" />
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog" />

    <link href="./authors.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Authors" />
  	<link href="./author.xml?id=${book.authors?first.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Toll, Ian W." />  

	<entry>
		<title>${book.title?xml}</title>
		<id>urn:book:${book.UUID}</id>
		<#list book.authors as author>
		<author>
			<name>${author.name?xml}</name>
			<uri>author.xml?id=${author.id?c}&amp;mode=${mode}</uri>
		</author>
		</#list>
		<updated>${book.lastModified?date}</updated>
		<#assign comment=book.calibreComments!""/>
	    <content type="text/html">${comment?xml}</content>
	    
	    <#list files as fi>
	    <#if fi.mimeType??>
	    <link href="./file?id=${book.id?c}&amp;ext=.${fi.extension?url}" type="${fi.mimeType?xml}" rel="http://opds-spec.org/acquisition" title="Download this ebook as ${fi.extension?upper_case?xml}" />
	    </#if>
		</#list>    
	    <link href="./image?id=${book.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
	    <link href="./image?id=${book.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />        
		<#list book.series as series>
		<link href="./series.xml?id=${series.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="related" title="Other books in the '${series.name}' series" />
		</#list>
		<#list book.authors as author>
		<link href="./author.xml?id=${author.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="related" title="Other books by ${author.name?xml}" />
		</#list>
		<#list book.tags as tag>
		<tag><id>${tag.id?c}</id><name>${tag.name}</name></tag>
		<link href="./tag.xml?id=${tag.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="related" title="Other titles with the tag '${tag.name}'" />
		</#list>
	</entry>
		
</feed>