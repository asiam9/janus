<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<#assign feed=doc/>
<html xmlns="http://www.w3.org/1999/xhtml" lang='en'>
	<head>
		<!-- encoding -->
		<meta charset="utf-8">

		<!-- basic viewport info -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		
		<!-- style -->
		<style type="text/css">
			body {
			    padding-top: 60px;
				padding-bottom: 40px;
			}
			.sidebar-nav {
				padding: 9px 0;
			}
		</style>
		<link href="style/enhanced/bootstrap.min.css" rel="stylesheet">
		<link href="style/enhanced/janus.css" rel="stylesheet">		
	
		<title>${feed.title[0]?html}</title>
	</head>
	<body>	
		<!--navigation -->
	    <div class="navbar navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </a>
	          <a class="brand" href="./index.html">Janus</a>
	          <div class="nav-collapse">
	            <ul class="nav pull-right">
	              <li class=""><a href="./author_index.html">Authors</a></li>
	              <li class=""><a href="./book_index.html">Books</a></li>
	              <li class=""><a href="./series_index.html">Series</a></li>
	              <li class=""><a href="./tag_index.html">Tags</a></li>
	            </ul>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
	    
	    <!-- content -->
	    <div class="container">
			<!-- header -->
			<header id='title'>
				<!-- title -->
    			<h2>${feed.title[0]?html}</h2>
			</header>

			<!-- paging row -->
			<div class='row'>
   				<!-- pages -->
    			<#assign totalPages=totalPages!0/>
    			<#assign currentPage=currentPage!0/>
    			<#assign pageSize=pageSize!0/>
    			<#assign sort=sort!"none"/>
    			<#if totalPages &gt; 0>
					<div class="pagination">
						<ul class="hidden-phone pull-right">
							<!-- if not on the first page, show first arrow -->
						  	<#if currentPage &gt; 0><li><a alt="first" href=".${path}?mode=enhanced&index=0&size=${pageSize}&sort=${sort}">&laquo;</a></li></#if>
						  	<#if currentPage &gt; 1><li><a alt="previous" href=".${path}?mode=enhanced&index=0&size=${pageSize * (currentPage - 1)}&sort=${sort}">&lt;</a></li></#if>
						  	<!-- list pages -->
						  	<#list 0..totalPages as page>
						  	<#if currentPage == page>
								<li class="active"><a href="#">${page + 1}</a></li>
							<#else>
								<li><a href=".${path}?mode=enhanced&index=${page * pageSize}&size=${pageSize}&sort=${sort}">${page+1}</a></li>
							</#if>
							</#list>
							<!-- if not on the last page, show end arrow -->
							<#if currentPage &lt; totalPages - 1><li><a alt="next" href=".${path}?mode=enhanced&index=${pageSize * (currentPage + 1)}&size=${pageSize}&sort=${sort}">&gt;</a></li></#if>
						  	<#if currentPage &lt; totalPages><li><a alt="last" href=".${path}?mode=enhanced&index=${pageSize * (totalPages)}&size=${pageSize}&sort=${sort}">&raquo;</a></li></#if>
						</ul>
						<ul class="visible-phone">
							<!-- if not on the first page, show first arrow -->
						  	<#if currentPage &gt; 0><li><a href=".${path}?mode=enhanced&index=0&size=${pageSize}&sort=${sort}">&laquo;</a></li></#if>
						  	<#if currentPage &gt; 1><li><a alt="previous" href=".${path}?mode=enhanced&index=0&size=${pageSize * (currentPage - 1)}&sort=${sort}">&lt;</a></li></#if>
						  	<!-- current page --> 
							<li class="active"><a href="#">${currentPage + 1} of ${totalPages + 1}</a></li>
							<!-- if not on the last page, show end arrow -->
							<#if currentPage &lt; totalPages - 1><li><a alt="next" href=".${path}?mode=enhanced&index=${pageSize * (currentPage + 1)}&size=${pageSize}&sort=${sort}">&gt;</a></li></#if>
						  	<#if currentPage &lt; totalPages><li><a alt="last" href=".${path}?mode=enhanced&index=${pageSize * (totalPages)}&size=${pageSize}&sort=${sort}">&raquo;</a></li></#if>
						</ul>						
					</div>
				<#else>
					<br/>									
				</#if>			
			</div>

			<!-- actual row -->
			<div class='row'>
				<#list feed.entry as entry>
					<#assign link=entry["link[contains(@type,'type=feed')]"]/>
					<#assign book=entry["link[contains(@type,'type=entry')]"]/>
					<#assign thumb=entry["link[contains(@rel,'thumbnail')]"]/>
					<#if link?size &gt; 0>
					<a href="${link[0].@href?replace(".xml",".html")?html}">
					<#elseif book?size &gt; 0>
					<a href="${book[0].@href?replace(".xml",".html")?html}&amp;type=book">
					<#else>
					</#if>
					<div class='span6'>
						<div class='well'>
							<div class='row-fluid'>
								<!-- show thumbnail if available -->
								<div class='span12'>
									<table>
										<tr>
											<#if thumb?size &gt; 0 && thumb[0].@href?length &gt; 1>										
											<td>
												<img class="thumbnail" src="${thumb[0].@href?html}"/>
											</td>
											</#if>
											<td valign='top' style='padding-left: 15px;'>
												<!-- show link to entry -->
												<#if link?size &gt; 0>
												<a href="${link[0].@href?replace(".xml",".html")?html}"><h3>${entry.title[0]?html}</h3></a>
												<#elseif book?size &gt; 0>
												<a href="${book[0].@href?replace(".xml",".html")?html}&amp;type=book"><h3>${entry.title[0]?html}</h3></a>
												<#else>
												</#if>
												<!-- description -->
												<p class='feedItemDescription'>${(entry.content[0]!"")?html}</p>
											</td>
										</tr>
									</table> <!-- closes table -->
								</div> <!-- closes span12 inside of fluid row -->
							</div> <!-- closes fluid row -->
						</div> <!-- closes well -->
					</div> <!-- closes span6 -->
					</a>
					<#if entry_index % 2 != 0>
						<!-- close row -->
						</div>
						<!-- open new -->
						<div class='row'>
					</#if>								
				</#list>
			</div> <!-- closes row that houses feed data -->
			
			<!-- stuff under the table -->
			<div class='row'>
			<!-- pagination, again -->
				<#if totalPages &gt; 0>
					<div class="pagination">
						<ul class="hidden-phone pull-right">
							<!-- if not on the first page, show first arrow -->
						  	<#if currentPage &gt; 0><li><a alt="first" href=".${path}?mode=enhanced&index=0&size=${pageSize}&sort=${sort}">&laquo;</a></li></#if>
						  	<#if currentPage &gt; 1><li><a alt="previous" href=".${path}?mode=enhanced&index=0&size=${pageSize * (currentPage - 1)}&sort=${sort}">&lt;</a></li></#if>
						  	<!-- list pages -->
						  	<#list 0..totalPages as page>
						  	<#if currentPage == page>
								<li class="active"><a href="#">${page + 1}</a></li>
							<#else>
								<li><a href=".${path}?mode=enhanced&index=${page * pageSize}&size=${pageSize}&sort=${sort}">${page+1}</a></li>
							</#if>
							</#list>
							<!-- if not on the last page, show end arrow -->
							<#if currentPage &lt; totalPages - 1><li><a alt="next" href=".${path}?mode=enhanced&index=${pageSize * (currentPage + 1)}&size=${pageSize}&sort=${sort}">&gt;</a></li></#if>
						  	<#if currentPage &lt; totalPages><li><a alt="last" href=".${path}?mode=enhanced&index=${pageSize * (totalPages)}&size=${pageSize}&sort=${sort}">&raquo;</a></li></#if>
						</ul>
						<ul class="visible-phone">
							<!-- if not on the first page, show first arrow -->
						  	<#if currentPage &gt; 0><li><a href=".${path}?mode=enhanced&index=0&size=${pageSize}&sort=${sort}">&laquo;</a></li></#if>
						  	<#if currentPage &gt; 1><li><a alt="previous" href=".${path}?mode=enhanced&index=0&size=${pageSize * (currentPage - 1)}&sort=${sort}">&lt;</a></li></#if>
						  	<!-- current page --> 
							<li class="active"><a href="#">${currentPage + 1} of ${totalPages + 1}</a></li>
							<!-- if not on the last page, show end arrow -->
							<#if currentPage &lt; totalPages - 1><li><a alt="next" href=".${path}?mode=enhanced&index=${pageSize * (currentPage + 1)}&size=${pageSize}&sort=${sort}">&gt;</a></li></#if>
						  	<#if currentPage &lt; totalPages><li><a alt="last" href=".${path}?mode=enhanced&index=${pageSize * (totalPages)}&size=${pageSize}&sort=${sort}">&raquo;</a></li></#if>
						</ul>						
					</div>
				</#if>
			</div>			
    	</div> <!-- /container -->
	
		<!-- bootstrap standard says place stuff at the end of the body -->
		<script src="jquery/jquery-1.7.2.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		
		<!-- init javascript stuff -->
	</body>
</html>