<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<cinemas>
			<xsl:for-each select="item">
				<cinema>
					<code><xsl:value-of select="cinemaId" /></code>
					<name><xsl:value-of select="cinemaName" /></name>
				</cinema>
			</xsl:for-each>
		</cinemas>
	</xsl:template>
</xsl:stylesheet>