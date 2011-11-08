<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Authors Index</title>
	
	<id>janus:authors</id>
	
	<content type="text">Index of authors</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#list authors as author>
	<entry>
		<#if sort=="sort">
		<title>${author.sortName?xml}</title>
		<#else>
		<title>${author.name?xml}</title>
		</#if>
		<id>janus:author:${author.id?c}</id>
		
		<#if sort=="name" || sort=="sort">
		<content type="text">Author of ${author.books?size} book<#if author.books?size &gt; 1>s</#if><#if author.series?size &gt; 1> in ${author.series?size} series</#if>.</content>
		</#if>
		<#if sort=="books">
		<content type="text">Author of ${author.books?size} book<#if author.books?size &gt; 1>s</#if>.</content>
		</#if>		
		<#if sort=="tags">
		<content type="text">Tagged with ${author.tags?size} tag<#if author.tags?size &gt; 1>s</#if>.</content>
		</#if>
		<#if sort=="series">
		<content type="text">Author of ${author.series?size} series.</content>
		</#if>
		
		<#if author.books?size &gt; 0>
		    <link href="./image?id=${author.books?first.id?c}" type="image/jpeg" rel="http://opds-spec.org/cover" />
		    <link href="./image?id=${author.books?first.id?c}&amp;type=thumbnail" type="image/jpeg" rel="http://opds-spec.org/thumbnail" />
	    </#if>  

		<link href="./author.xml?id=${author.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>	
	</entry>
	</#list>
	
	<#if sort=="books" && mode!="json">
	<link href="./authors.xml?tag=${tag?c!0}&amp;sort=${sort}&amp;index=${index?c}&amp;size=${size?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog" rel="next" title="next page"/>
	</#if>
</feed>