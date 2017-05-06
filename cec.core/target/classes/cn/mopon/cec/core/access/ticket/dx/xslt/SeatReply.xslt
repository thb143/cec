<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<seats>	
			<xsl:for-each select="item">
				<seat>
					<code><xsl:value-of select="cineSeatId" /></code>
					<xCoord><xsl:value-of select="xCoord" /></xCoord>
				    <yCoord><xsl:value-of select="yCoord" /></yCoord>
				    <rowNum><xsl:value-of select="row" /></rowNum>
				    <colNum><xsl:value-of select="column" /></colNum>
				    <loveCode><xsl:value-of select="loveseats" /></loveCode>
				    <status>
						<xsl:choose>
							<xsl:when test="status='ok'">1</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</status>
					<type>
						<xsl:choose>
							<xsl:when test="type='shuangren'">2</xsl:when>
							<xsl:otherwise>1</xsl:otherwise>
						</xsl:choose>
					</type>
				</seat>
			</xsl:for-each>
		</seats>		
	</xsl:template>
</xsl:stylesheet>