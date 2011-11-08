<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>${author.name?xml}</title>
	<id>urn:author:${author.id?c}</id>
 	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus Catalog" />
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog" />
	<#list series as ser>
	<entry>
		<title>Series: ${ser.name?xml}</title>
		<id>janus:series:${ser.id?c}</id>
		<#if ser.books?size &gt; 1>
		<content type="text">
			Contains ${ser.books?size} books from ${ser.books?first.title?xml} to ${ser.books?last.title?xml}.		
		</content>
		<#else>
		<content type="text">
			${ser.books?first.title?xml}		
		</content>
		</#if>
		<link href="./series.xml?id=${ser.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="alternate"/>
		<#if mode != "aldiko">
		<link href="./series.xml?id=${ser.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	    <link href="./image?id=${ser.books?first.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
		</#if>
	    <link href="./image?id=${ser.books?first.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />
	</entry>      
	</#list>
	<#list books as book>
	<entry>
		<title>${book.title?xml}</title>
		<id>janus:book:${book.id?c}</id>
		
		<author>
			<name>${author.name?xml}</name>
			<uri>author.xml?id=${author.id?c}&amp;mode=${mode}</uri>
		</author>
		<updated>${book.lastModified?date}</updated>
		<link href="./book.xml?id=${book.id?c}&amp;mode=${mode}" type="application/atom+xml;type=entry;profile=opds-catalog" rel="alternate"/>
		<#if mode != "aldiko">
		<link href="./book.xml?id=${book.id?c}&amp;mode=${mode}" type="application/atom+xml;type=entry;profile=opds-catalog"/>
	    <link href="./image?id=${book.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
		</#if>			
	    <link href="./image?id=${book.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />      
	</entry>
	</#list>
</feed>