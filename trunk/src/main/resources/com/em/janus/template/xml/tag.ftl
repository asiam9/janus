<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Index for ${tag.name?xml}</title>
	
	<id>janus:tag:${tag.id?c}</id>
	
	<content type="text">index of tag ${tag.name?xml}</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="breadcrumb" title="Janus catalog"/>
	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus catalog"/>

	<#if authors?size &gt; 1>
	<entry>
		<title>Authors of this tag</title>
		<id>janus:tags:authors</id>

		<content type="text">
			Link to the ${authors?size} authors who contributed to ${tag.name?xml}		
		</content>
		
		<link href="./authors.xml?tag=${tag.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	<#elseif authors?size == 1>
	<entry>
		<title>${authors?first.name?xml}</title>
		<id>janus:tags:authors</id>

		<content type="text">
			Link to the single author of this tag.		
		</content>
		
		<link href="./author.xml?id=${authors?first.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	</#if>

	<#if books?size &gt; 1>
	<entry>
		<title>Books tagged with this tag</title>
		<id>janus:tags:books</id>

		<content type="text">
			Link to the ${books?size} books tagged with this tag ${tag.name?xml}		
		</content>
		
		<link href="./books.xml?tag=${tag.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	<#elseif books?size == 1>
	<entry>
		<title>${books?first.name?xml}</title>
		<id>janus:tags:books</id>

		<content type="text">
			Link to the single book tagged with this tag.		
		</content>
		
		<link href="./book.xml?id=${book?first.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	</#if>
	
	<#if series?size &gt; 1>
	<entry>
		<title>Series tagged with this tag</title>
		<id>janus:tags:series</id>

		<content type="text">
			Link to the ${series?size} series tagged with this tag ${tag.name?xml}		
		</content>
		
		<link href="./series_list.xml?tag=${tag.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	<#elseif series?size == 1>
	<entry>
		<title>${series?first.name?xml}</title>
		<id>janus:tags:series</id>

		<content type="text">
			Link to the single series tagged with this tag.		
		</content>
		
		<link href="./series.xml?id=${series?first.id?c}&amp;mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
	</entry>
	</#if>

</feed>