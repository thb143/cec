<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data/seatLock">
		<result>
			<xsl:value-of select="result" />
		</result>
		<message>
			<xsl:value-of select="messages/message" />
		</message>
	</xsl:template>
</xsl:stylesheet>


