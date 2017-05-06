<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/reply">
		<reply>
			<code><xsl:value-of select="code" /></code>
			<msg><xsl:value-of select="msg" /></msg>
			<seats>
				<xsl:for-each select="seats/seat">
					<seat>
					 	<code><xsl:value-of select="code" /></code>
					 	<groupCode><xsl:value-of select="groupCode" /></groupCode>
					 	<rowNum><xsl:value-of select="rowNum" /></rowNum>
					 	<colNum><xsl:value-of select="colNum" /></colNum>
					 	<xCoord><xsl:value-of select="xCoord" /></xCoord>
					 	<yCoord><xsl:value-of select="yCoord" /></yCoord>
					 	<type><xsl:value-of select="type" /></type>
					 	<status><xsl:value-of select="status" /></status>
					 	<loveCode><xsl:value-of select="loveCode" /></loveCode>
					</seat>
				</xsl:for-each>
			</seats>
		</reply>
	</xsl:template>
</xsl:stylesheet>