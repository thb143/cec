<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root">
		<root>
			<status><xsl:value-of select="status" /></status>
			<errorCode><xsl:value-of select="errorCode" /></errorCode>
			<errorMessage><xsl:value-of select="errorMessage" /></errorMessage>
		</root>
	</xsl:template>
</xsl:stylesheet>