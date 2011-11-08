<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Tag Category Index</title>
	
	<id>janus:series</id>
	
	<content type="text">Index of all tags, categorized alphabetically</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#list sections as section>
	<entry>
		<title>${section.name?xml}</title>
		<id>janus:tag:section:${section.name?lower_case?xml}</id>
		
		<#if section.contents?size == 1>
		<content type="text">${section.contents?first.name?xml}</content>
		<#elseif section.contents?size &gt; 1>
		<content type="text">${section.contents?size} tags from ${section.contents?first.name?xml} to ${section.contents?last.name?xml}</content>
		<#else>
		<content type="text">No tags in this section.</content>
		</#if>
		
		<link href="./tag_list.xml?starts=${section.id?xml}&amp;mode=${mode}&amp;sort=${sort}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>	
	</entry>
	</#list>
	
</feed>