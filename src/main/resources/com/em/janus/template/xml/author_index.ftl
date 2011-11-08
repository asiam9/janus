<?xml version="1.0" encoding="UTF-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:opds="http://opds-spec.org/2010/catalog">
	<title>Janus Author Index</title>
	
	<id>janus:catalog</id>
	<content type="text">cataloged ${category?size} catalogs</content>

	<link href="./index.xml" type="application/atom+xml;type=feed;profile=opds-catalog" rel="start" title="Janus Catalog Index"/>

	<entry>
		<title>By last name</title>
		<id>janus:authors:sort</id>
		<content type="text">Alphabetical, by last name, index of ${category?size} authors.</content>
		<link href="./author_sections.xml?mode=${mode}&amp;sort=sort" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry>
		<title>By first name</title>
		<id>janus:authors:name</id>
		<content type="text">Alphabetical index of ${category?size} authors.</content>
		<link href="./author_sections.xml?mode=${mode}&amp;sort=name" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>

	<entry>
		<title>By prolificness</title>
		<id>janus:authors:books</id>
		<content type="text">Index of ${category?size} authors by books written.</content>
		<link href="./authors.xml?mode=${mode}&amp;sort=books" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>	
	
	<entry>
		<title>By most tagged</title>
		<id>janus:authors:tags</id>
		<content type="text">Index of ${category?size} authors by total number of tags.</content>
		<link href="./authors.xml?mode=${mode}&amp;sort=tags" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>	
	
	<entry>
		<title>By most series</title>
		<id>janus:authors:tags</id>
		<content type="text">Index of ${category?size} authors by most series.</content>
		<link href="./authors.xml?mode=${mode}&amp;sort=series" type="application/atom+xml;type=feed;profile=opds-catalog"/>
		<link href="" type="image/png" rel="http://opds-spec.org/thumbnail"/>
	</entry>	

</feed>