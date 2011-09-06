<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Series List Index</title>
	
	<id>janus:series</id>
	
	<content type="text">index of series</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#list series as ser>
	<entry>
		<title>${ser.name?xml}</title>
		<id>janus:ser:${ser.id?c}</id>
		<#list ser.authors as author>
		<author>
			<name>${author.name?xml}</name>
			<uri>./author.xml?id=${author.id?c}&amp;mode=${mode}</uri>
		</author>
		</#list>
		
		<content type="text">
			Contains ${ser.books?size} books written by<#list ser.authors as author> ${author.name?xml}</#list>		
		</content>
		
		<link href="./series.xml?id=${ser.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="alternate"/>
		<link href="./series.xml?id=${ser.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		
	    <link href="./image?id=${ser.books?first.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
	    <link href="./image?id=${ser.books?first.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />      
	</entry>
	</#list>
	
	<#if sort=="books">
	<link href="./books.xml?sort=${sort}&amp;index=${index?c}&amp;size=${size?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="next" title="next page"/>
	</#if>
	
</feed>