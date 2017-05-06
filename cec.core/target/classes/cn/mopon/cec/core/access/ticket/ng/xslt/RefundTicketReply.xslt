<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/RefundTicketReply/Order">
		<refunded>
			<xsl:choose>
				<xsl:when test="Status='Yes'">true</xsl:when>
				<xsl:when test="Status='No'">false</xsl:when>
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
		</refunded>
	</xsl:template>
</xsl:stylesheet>