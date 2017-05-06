<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/LoginCardReturn">
		<CardInfo>
			 <code><xsl:value-of select="ResultCode"/></code>
			 <card>	
			      <cinemaCode><xsl:value-of select="PlaceNo"/></cinemaCode>
			      <cardCode><xsl:value-of select="CardId"/></cardCode>
			      <balance>
			       		<xsl:choose>
								<xsl:when test="AccBalance = ''">0</xsl:when>
								<xsl:otherwise><xsl:value-of select="AccBalance"/></xsl:otherwise>
						</xsl:choose>
			      </balance>
			      <score>
			       		<xsl:choose>
								<xsl:when test="AccIntegral = ''">0</xsl:when>
								<xsl:otherwise><xsl:value-of select="AccIntegral"/></xsl:otherwise>
						</xsl:choose>
			      </score>
			      <accLevelName><xsl:value-of select="AccLevelName"/></accLevelName>
			      <expirationTime><xsl:value-of select="ExpirationTime"/> 00:00:00</expirationTime>
			      <status><xsl:value-of select="AccStatus"/></status>
			  </card>
		</CardInfo>
	</xsl:template>
</xsl:stylesheet>