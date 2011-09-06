<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Tag Index</title>
	
	<id>janus:catalog:tag</id>
	<content type="text">with ${category?size} tags</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus Catalog Index"/>

	<entry>
		<title>By tag name</title>
		<id>janus:tag:name</id>
		<content type="text">Alphabetical index of ${category?size} tags.</content>
		<link href="./tags.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>
	
	<entry>
		<title>By number of books tagged</title>
		<id>janus:tag:books</id>
		<content type="text">Index of ${category?size} tags ordered by number of books tagged.</content>
		<link href="./tags.xml?mode=${mode}&amp;sort=books" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry> 	
		<title>By number of series tagged</title>
		<id>janus:tag:series</id>
		<content type="text">Index of ${category?size} tags ordered by number of series tagged.</content>
		<link href="./tags.xml?mode=${mode}&amp;sort=series" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>
	
</feed>