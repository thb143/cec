<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<shows>
			<xsl:for-each select="item">
				<show>
					<xsl:variable name="type" select="substring(movieInfo/item/cineMovieNum,4,1)"></xsl:variable>
					<xsl:variable name="updateTime"	select="translate(translate(translate(cineUpdateTime,'-',''),':',''),' ','')" />
					<code>
						<xsl:value-of select="id" />
						<xsl:choose>
							<xsl:when test="$updateTime='0000-00-00 00:00:00'">00000000000000</xsl:when>
							<xsl:otherwise><xsl:value-of select="$updateTime" /></xsl:otherwise>
						</xsl:choose>
					</code>
					<hall>
						<code><xsl:value-of select="hallId" /></code>
					</hall>
					<filmCode><xsl:value-of select="movieInfo/item/cineMovieNum" /></filmCode>
					<language><xsl:value-of select="movieInfo/item/movieLanguage" /></language>
					<showType>
						<xsl:choose>
							<xsl:when test="$type='1'">1</xsl:when>
							<xsl:when test="$type='2'">2</xsl:when>
							<xsl:when test="$type='3'">3</xsl:when>
							<xsl:when test="$type='4'">4</xsl:when>
							<xsl:when test="$type='6'">6</xsl:when>
							<xsl:when test="$type='8'">6</xsl:when>
							<xsl:when test="$type='9'">6</xsl:when>
							<xsl:when test="$type='i'">6</xsl:when>
							<xsl:when test="$type='j'">6</xsl:when>
							<xsl:when test="$type='b'">1</xsl:when>
							<xsl:when test="$type='c'">2</xsl:when>
							<xsl:when test="$type='m'">2</xsl:when>
							<xsl:when test="$type='w'">2</xsl:when>
							<xsl:when test="$type='d'">3</xsl:when>
							<xsl:when test="$type='n'">3</xsl:when>
							<xsl:when test="$type='x'">3</xsl:when>
							<xsl:when test="$type='e'">4</xsl:when>
							<xsl:when test="$type='o'">4</xsl:when>
							<xsl:when test="$type='y'">4</xsl:when>
							<xsl:when test="$type='C'">6</xsl:when>
							<xsl:when test="$type='D'">6</xsl:when>
							<xsl:otherwise>1</xsl:otherwise>
						</xsl:choose>
					</showType>
					<showTime><xsl:value-of select="startTime" /></showTime>
					<startTime><xsl:value-of select="startTime" /></startTime>
					<endTime><xsl:value-of select="endTime" /></endTime>
					<minPrice><xsl:value-of select="lowestPrice" /></minPrice>
					<stdPrice><xsl:value-of select="marketPrice" /></stdPrice>
					<through>false</through>
				</show>
			</xsl:for-each>
		</shows>
	</xsl:template>
</xsl:stylesheet>