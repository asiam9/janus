<?xml version="1.0" encoding="UTF-8"?>
<!-- <?xml-stylesheet href="./style/xml/index.xml.css" type="text/css"?> -->
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Catalog Index</title>
	
	<id>janus:catalog</id>
	<content type="text">cataloged ${books?size} books</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus Catalog Index"/>

	<entry>
		<title>Authors</title>
		<id>janus:authors</id>
		<content type="text">Indices of ${authors?size} authors.</content>
		<link href="./author_index.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>
	
	<entry>
		<title>Books</title>
		<id>janus:books</id>
		<content type="text">Indices of ${books?size} books.</content>
		<link href="./book_index.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry> 	
		<title>Series</title>
		<id>janus:series</id>
		<content type="text">Indices of ${series?size} series.</content>
		<link href="./series_index.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry>
		<title>Tags</title>
		<id>janus:tags</id>
		<content type="text">Indices index of ${tags?size} tags.</content>
		<link href="./tag_index.xml?mode=${mode}" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>
	
</feed>