<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Tag List Index</title>
	
	<id>janus:tags</id>
	
	<content type="text">index of tags</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#list tags as tag>
	<entry>
		<title>${tag.name?xml}</title>
		<id>janus:tag:${tag.id?c}</id>
		<#list tag.authors as author>
		<author>
			<name>${author.name?xml}</name>
			<uri>./author.xml?id=${author.id?c}&amp;mode=${mode}</uri>
		</author>
		</#list>
		
		<content type="text">
			Contains ${tag.books?size} books, ${tag.series?size} series, by ${tag.authors?size} authors.	
		</content>
		
		<link href="./tag.xml?id=${tag.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="alternate"/>
		<link href="./tag.xml?id=${tag.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		
		<#if tag.books?size &gt; 0>
		    <link href="./image?id=${tag.books?first.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
		    <link href="./image?id=${tag.books?first.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />
	    </#if>      
	</entry>
	</#list>
	
	<#if sort=="books">
	<link href="./books.xml?sort=${sort}&amp;index=${index?c}&amp;size=${size?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="next" title="next page"/>
	</#if>
	
</feed>