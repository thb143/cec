<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<showSeats>	
			<xsl:for-each select="item">
				<showSeat>
					<code><xsl:value-of select="cineSeatId" /></code>
					<xCoord><xsl:value-of select="x" /></xCoord>
					<yCoord><xsl:value-of select="y" /></yCoord>
					<rowNum><xsl:value-of select="rowValue" /></rowNum>
					<colNum><xsl:value-of select="columnValue" /></colNum>
					<status>
						<xsl:choose>
							<xsl:when test="seatStatus='ok'">1</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</status>
				</showSeat>
			</xsl:for-each>
		</showSeats>		
	</xsl:template>
</xsl:stylesheet>