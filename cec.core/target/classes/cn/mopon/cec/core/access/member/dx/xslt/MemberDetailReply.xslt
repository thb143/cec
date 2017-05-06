<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<memberCard>
			<balance>
	       		<xsl:choose>
						<xsl:when test="balance=''">0</xsl:when>
						<xsl:otherwise><xsl:value-of select="balance"/></xsl:otherwise>
				</xsl:choose>
		    </balance>
		    <score>
	       		<xsl:choose>
						<xsl:when test="availableJifen=''">0</xsl:when>
						<xsl:otherwise><xsl:value-of select="availableJifen"/></xsl:otherwise>
				</xsl:choose>
		    </score>
			<accLevelName><xsl:value-of select="cardLevel" /></accLevelName>
			<expirationTime><xsl:value-of select="period" /></expirationTime>
			<status><xsl:value-of select="canUse" /></status>
		</memberCard>
	</xsl:template>
</xsl:stylesheet>