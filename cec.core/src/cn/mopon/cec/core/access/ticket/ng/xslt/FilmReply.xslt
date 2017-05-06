<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/QueryFilmReply/Films">
		<films>
			<xsl:for-each select="Film">
				<Film>
					<code><xsl:value-of select="Code" /></code>
					<name><xsl:value-of select="Name" /></name>
					<version><xsl:value-of select="Version" /></version>
					<duration>
						<xsl:choose>
							<xsl:when test="Duration=''">0</xsl:when>
							<xsl:otherwise><xsl:value-of select="Duration" /></xsl:otherwise>
						</xsl:choose>
					</duration>
					<publishDate><xsl:value-of select="PublishDate" /></publishDate>
					<publisher>
						<xsl:if test="Publisher!='暂空'">
							<xsl:value-of select="Publisher" />
						</xsl:if>
					</publisher>
					<producer>
						<xsl:if test="Producer!='Unknown'">
							<xsl:value-of select="Producer" />
						</xsl:if>
					</producer>
					<director>
						<xsl:if test="Director != 'Unknown'">
							<xsl:value-of select="Director" />
						</xsl:if>
					</director>
					<cast>
						<xsl:if test="Cast != 'Unknown'">
							<xsl:value-of select="Cast" />
						</xsl:if>
					</cast>
					<intro>
						<xsl:if test="Introduction != 'Unknown'">
							<xsl:value-of select="Introduction" />
						</xsl:if>
					</intro>
				</Film>
			</xsl:for-each>
		</films>
	</xsl:template>
</xsl:stylesheet>