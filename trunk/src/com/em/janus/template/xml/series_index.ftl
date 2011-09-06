<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Catalog Index</title>
	
	<id>janus:catalog</id>
	<content type="text">cataloged ${category?size} series</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus Catalog Index"/>
	<entry>
		<title>By series name</title>
		<id>janus:series:name</id>
		<content type="text">Alphabetical index of ${category?size} books.</content>
		<link href="./series_sections.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry> 	
		<title>By number of books</title>
		<id>janus:series:books</id>
		<content type="text">Index of ${category?size} series by number of books.</content>
		<link href="./series_list.xml?mode=${mode}&amp;sort=books" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry> 	
		<title>By most recently added book</title>
		<id>janus:series:books</id>
		<content type="text">Index of ${category?size} series by the most recently added series items.</content>
		<link href="./series_list.xml?mode=${mode}&amp;sort=date" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	
</feed>