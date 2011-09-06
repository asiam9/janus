<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Catalog Index</title>
	
	<id>janus:catalog</id>
	<content type="text">cataloged ${category?size} books</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus Catalog Index"/>

	<entry>
		<title>By title</title>
		<id>janus:authors:name</id>
		<content type="text">Alphabetical index of ${category?size} books.</content>
		<link href="./book_sections.xml?mode=${mode}&amp;sort=name" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>
	
	<entry>
		<title>By most recently added</title>
		<id>janus:authors:name</id>
		<content type="text">Index of ${category?size} most recently added books.</content>
		<link href="./books.xml?mode=${mode}&amp;sort=recent" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry>
		<title>By most tagged</title>
		<id>janus:authors:sort</id>
		<content type="text">Index of ${category?size} books by most tagged.</content>
		<link href="./books.xml?mode=${mode}&amp;sort=tags" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>
	
</feed>